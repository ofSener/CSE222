# core_solver/utils.py

import sympy
import math
from sympy import Symbol, Integer, zoo

def safe_fraction(expr):
    """
    Sympy'nin fraction(expr) fonksiyonu, ifadenin pay ve paydasini dondurur.
    Ancak ifade rasyonel degilse, fraction(expr) hata verebilir.
    Bu fonksiyon, bir try/except ile 'pay', 'payda' elde etmeye calisir.
    Rasyonel degilse pay=expr, payda=1 olarak kabul eder.
    """
    try:
        numer, denom = sympy.fraction(expr)
        return numer, denom
    except:
        return expr, Integer(1)

def is_zero(val):
    """
    val'in 0 olup olmadigini mumkun oldugunca guvenli sekilde anlar.
    val bazen symbolic kalabilir, evalf() cagirip 0'a yakin mi diye bakariz.
    """
    try:
        return abs(val.evalf()) < 1e-12
    except:
        return False

def is_infinite(val):
    """
    val sonsuz mu (infinite) diye bakar. Sympy 'zoo', 'oo' gibi tipleri olabilir.
    Ya da evalf() cok buyuk bir sayi dondurebilir.
    """
    if val is zoo:
        return True
    if getattr(val, 'is_infinite', False):
        return True
    try:
        if abs(val.evalf()) > 1e14:
            return True
    except:
        pass
    return False

def detect_indeterminate(expr, var, point):
    """
    Gelismis belirsizlik tespit fonksiyonu:
    - 0/0 => '0/0'
    - sonsuz/sonsuz => '∞/∞'
    - 1^∞, 0^0, ∞^0
    - expr.subs(...) sonrasi NaN => genelde 0/0
    - expr.subs(...) sonrasi zoo => ∞/∞
    """
    # 1) Ustel belirsizlikler (base^exp) kontrolu
    if expr.is_Pow:
        base, exponent = expr.as_base_exp()
        try:
            base_val = float(base.subs(var, point).evalf())
        except:
            base_val = None
        try:
            exp_val = float(exponent.subs(var, point).evalf())
        except:
            exp_val = None

        # 1^∞
        if base_val is not None and math.isclose(base_val, 1.0, abs_tol=1e-12) and \
           exp_val is not None and abs(exp_val) > 1e12:
            return "1^∞"
        # 0^0
        if base_val is not None and exp_val is not None:
            if math.isclose(base_val, 0.0, abs_tol=1e-12) and math.isclose(exp_val, 0.0, abs_tol=1e-12):
                return "0^0"
        # ∞^0
        if base_val is not None and exp_val is not None:
            if (math.isinf(base_val) or abs(base_val) > 1e12) and math.isclose(exp_val, 0.0, abs_tol=1e-12):
                return "∞^0"

    # 2) Dogrudan ifadeyi evaluate edelim
    try:
        val = expr.subs(var, point).evalf()
        fval = float(val)
    except:
        return "unknown"

    # 3) 0/0 => genelde math.isnan
    if math.isnan(fval):
        return "0/0"

    # 4) ∞/∞ => math.isinf
    if math.isinf(fval):
        return "∞/∞"

    # 5) Deger sonlu bir sayi ise belirsizlik yok
    return None

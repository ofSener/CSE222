# core_solver/rules.py

import sympy
import math
from sympy import diff, series, sin, Symbol, limit as sympy_limit
from .utils import safe_fraction, detect_indeterminate
from ..cxx_interface import is_polynomial_expr, derivative_via_cpp

def apply_lhopital_once(expr, var):
    """
    Tek seferlik L'Hôpital.
    Polinom ise C++ türev, değilse Sympy diff
    """
    numer, denom = safe_fraction(expr)
    # Polinom mu?
    if is_polynomial_expr(numer, var) and is_polynomial_expr(denom, var):
        # C++ türev
        d_numer = derivative_via_cpp(numer, var)
        d_denom = derivative_via_cpp(denom, var)
    else:
        # fallback: normal Sympy diff
        d_numer = diff(numer, var)
        d_denom = diff(denom, var)
    new_expr = d_numer / d_denom
    deriv_info = f"(numer diff, denom diff) => ({d_numer}, {d_denom})"
    return new_expr, deriv_info

def apply_lhopital_repeated(expr, var, point, steps_list, step_count, max_repeats=10):
    """
    0/0 veya ∞/∞ belirsizligi bitene kadar L'Hôpital'i tekrarla.
    steps_list'e adimlar ekleyip step_count'u arttirir.
    Dönüş: (final_expr, updated_step_count)
    """
    current_expr = expr
    for _ in range(max_repeats):
        ind = detect_indeterminate(current_expr, var, point)
        if ind in ("0/0", "∞/∞"):
            new_expr, deriv_info = apply_lhopital_once(current_expr, var)
            steps_list.append({
                "step_number": step_count,
                "rule": "L'Hopital",
                "old_expression": str(current_expr),
                "new_expression": str(new_expr),
                "explanation": f"{ind} belirsizligi, L'Hôpital uygulandi. ({deriv_info})"
            })
            step_count += 1
            current_expr = new_expr
        else:
            # 0/0 veya ∞/∞ disinda kaldiginda dur
            break

    return current_expr, step_count

def apply_trig_known(expr, var, point):
    """
    Örnek: sin(x)/x, x->0 => 1.
    Geri: (True, 1) veya (False, None).
    """
    if point == 0 and expr == sin(var)/var:
        return True, sympy.Integer(1)
    return False, None

def apply_series_expansion(expr, var, point, order=5):
    """
    'point' etrafinda 'order' terimine kadar seri acilim.
    O(...) terimini removeO() ile cikarir.
    """
    s = series(expr, var, point, order)
    return s.removeO()

def apply_exponential_belirsizlik(expr, var, point):
    """
    1^∞, 0^0, ∞^0 gibi durumlara log-transform yaklaşimi:
      limit(expr) = exp( limit(ln(expr)) ).
    Geri: (ln_limit, final_val)
    """
    log_expr = sympy.log(expr)
    ln_lim = sympy_limit(log_expr, var, point)
    final_val = sympy.exp(ln_lim)
    return ln_lim, final_val

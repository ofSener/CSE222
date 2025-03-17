# python/cxx_interface.py
import sys
import os

cxx_path = os.path.join(os.path.dirname(__file__), '..', 'cxx')
sys.path.append(os.path.abspath(cxx_path))

import polynomial
import sympy

def from_sympy_poly(sympy_poly, var):
    """
    sympy_poly: sympy.Poly nesnesi
    var: sympy Symbol
    Dönüş: polynomial.Polynomial (C++ objesi)
    """
    # sympy_poly.all_coeffs() -> [leading, ..., constant]
    # biz polynomial modülünde [a0, a1, a2,...] (constant -> leading) formu istiyoruz
    c = sympy_poly.all_coeffs()  # örn. x^2 + 2x + 3 => [1, 2, 3]
    c = list(reversed(c))        # => [3, 2, 1]
    return polynomial.Polynomial(c)

def to_sympy_poly(cpp_poly, var):
    """
    cpp_poly: polynomial.Polynomial (C++)
    var: sympy Symbol
    Dönüş: sympy.Poly
    """
    c = cpp_poly.getCoeffs()  # [a0, a1, a2, ...]
    c_rev = list(reversed(c)) # [leading, ... , a0]
    return sympy.Poly(c_rev, var)

def is_polynomial_expr(expr, var):
    """
    expr: Sympy ifadesi
    var: Symbol
    Döner: True/False => 'expr' saf polinom mu?
    """
    # sympy.poly(...) ile test. poly(...) hata verirse polinom degil demektir.
    try:
        p = sympy.poly(expr, var)
        # p.is_polynomial = True => polinom
        # or we can check p.degree() directly
        return True
    except sympy.PolynomialError:
        return False

def derivative_via_cpp(expr, var):
    """
    Polinom ifadesi => C++ ile türev hesaplama => Sympy ifadesi döndür.
    """
    poly_expr = sympy.poly(expr, var)
    cpp_p = from_sympy_poly(poly_expr, var)
    cpp_deriv = cpp_p.derivative()  # C++ tarafinda türev
    return to_sympy_poly(cpp_deriv, var).as_expr()  # Sympy ifadesi


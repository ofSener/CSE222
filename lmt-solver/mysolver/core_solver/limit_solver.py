# mysolver/core_solver/limit_solver.py

import sympy
import math
from sympy import Symbol, limit as sympy_limit, simplify
from .utils import detect_indeterminate

from mysolver.manual_factor import manual_factor_simplify


# eğer L'Hopital, trig rules vs. varsa:
from .rules import (
    apply_lhopital_repeated,
    apply_exponential_belirsizlik,
    apply_trig_known,
    apply_series_expansion
)

def solve_limit_step_by_step(expression_str, var_str, limit_point, max_loops=10):
    """
    Gelişmiş step-by-step limit çözümü:
      1) detect_indeterminate => '0/0' vs. '∞/∞', ...
      2) Rasyonelse factor/cancel => adım kaydet
      3) 'sin(x)/x' => trig rule, ...
      4) L'Hopital => vs.
      5) final => simplify + substitution
    """
    steps = []
    step_count = 1

    var = Symbol(var_str, real=True)
    expr = sympy.sympify(expression_str)
    current_expr = expr

    # 1) trig known check
    used_trig, trig_val = apply_trig_known(current_expr, var, limit_point)
    if used_trig:
        steps.append({
            "step_number": step_count,
            "rule": "Trig Rule",
            "old_expression": str(current_expr),
            "new_expression": str(trig_val),
            "explanation": "Özel trig kuralı (sin(x)/x => 1)"
        })
        step_count += 1
        current_expr = trig_val

    # 2) detect belirsizlik
    for _ in range(max_loops):
        ind = detect_indeterminate(current_expr, var, limit_point)

        if ind is None:
            # Belirsizlik yok => substitution
            val = current_expr.subs(var, limit_point)
            steps.append({
                "step_number": step_count,
                "rule": "Substitution",
                "old_expression": str(current_expr),
                "new_expression": str(val),
                "explanation": f"{var_str} -> {limit_point} => {val}"
            })
            step_count += 1
            break

        elif ind == "0/0":
            # => önce factor/cancel denemek
            # pay, den
            # factor => cancel => adımlar
            canceled_expr, step_count = manual_factor_simplify(current_expr, var, steps, step_count)
            # yeni ifade
            current_expr = canceled_expr

            # belki yine belirsizlik bitmemiş olabilir, döngü tekrarı

        elif ind in ("∞/∞"):
            # L'Hopital tekrarlı
            new_expr, step_count = apply_lhopital_repeated(current_expr, var, limit_point, steps, step_count)
            current_expr = new_expr

        elif ind in ("1^∞", "0^0", "∞^0"):
            # log-transform
            ln_lim, final_val = apply_exponential_belirsizlik(current_expr, var, limit_point)
            steps.append({
                "step_number": step_count,
                "rule": "Exponential Belirsizlik",
                "old_expression": str(current_expr),
                "new_expression": str(final_val),
                "explanation": f"{ind} => log(...) => {ln_lim}, exp(...) => {final_val}"
            })
            step_count += 1
            break

        else:
            # unknown => belki series expansion, vb.
            steps.append({
                "step_number": step_count,
                "rule": "Series Attempt or SympyDirect",
                "old_expression": str(current_expr),
                "new_expression": "",
                "explanation": f"Bilinmeyen: {ind}"
            })
            step_count += 1
            # mesela direct sympy.limit
            direct_val = sympy_limit(current_expr, var, limit_point)
            steps.append({
                "step_number": step_count,
                "rule": "SympyDirect",
                "old_expression": str(current_expr),
                "new_expression": str(direct_val),
                "explanation": "Sympy.limit(...)"
            })
            step_count += 1
            break

    # 3) final check
    final_limit = sympy_limit(expr, var, limit_point)
    final_limit = simplify(final_limit)
    steps.append({
        "step_number": step_count,
        "rule": "Final Check",
        "old_expression": str(expr),
        "new_expression": str(final_limit),
        "explanation": f"Orijinal ifadenin limiti: {final_limit}"
    })

    return final_limit, steps

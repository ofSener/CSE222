# python/main.py

import sys
from core_solver.limit_solver import solve_limit_step_by_step

if __name__ == "__main__":
    if len(sys.argv) < 4:
        expr_str = "(x^2 - 1)/(x - 1)"
        var_str = "x"
        limit_pt = 1
    else:
        expr_str = sys.argv[1]
        var_str = sys.argv[2]
        limit_pt = float(sys.argv[3])

    result, steps = solve_limit_step_by_step(expr_str, var_str, limit_pt)
    print("RESULT:", result)
    print("STEPS:")
    for st in steps:
        print(f"  Step {st['step_number']} - {st['rule']}")
        print(f"    Old: {st['old_expression']}")
        print(f"    New: {st['new_expression']}")
        print(f"    Explanation: {st['explanation']}\n")

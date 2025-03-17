# python/api.py

from flask import Flask, request, jsonify, session

from mysolver.core_solver.limit_solver import solve_limit_step_by_step
import sympy
from flask_cors import CORS





app = Flask(__name__)
CORS(app)
app.secret_key = "CHANGE_THIS_TO_A_SECURE_RANDOM_KEY"  # Oturum yönetimi için gizli anahtar

# Örnek/dummy kullanıcı verileri (gerçek uygulamada DB'den gelir)
USERS = {
    "alice": "password123",
    "bob": "secret"
}


@app.route("/login", methods=["POST"])
def login():
    """
    Basit login örneği:
      JSON body: { "username": "...", "password": "..." }
    Gerçek uygulamada veritabanı kontrolü, hashing vb.
    """
    data = request.get_json()
    username = data.get("username")
    password = data.get("password")
    if username in USERS and USERS[username] == password:
        session["logged_in"] = True
        session["username"] = username
        return jsonify({"message": f"Welcome, {username}!", "login": True})
    else:
        return jsonify({"message": "Invalid credentials", "login": False}), 401


@app.route("/logout", methods=["POST"])
def logout():
    """
    Kullanıcı oturumunu kapatır.
    """
    session.clear()
    return jsonify({"message": "Logged out", "login": False})


@app.route("/solve-limit", methods=["POST"])
def solve_limit():
    """
    Ana endpoint: Limit hesabı, adım adım açıklamalar + Sympy.latex dönüşü.
    
    Gönderilecek JSON:
      {
        "expression": "(x^2 - 1)/(x - 1)",
        "variable": "x",
        "limit_point": "1"
      }
    Dönen JSON:
      {
        "result": "2",
        "result_latex": "\\mathrm{2}",
        "steps": [
          {
            "step_number": 1,
            "rule": "L'Hopital",
            "old_expression": "(x^2 - 1)/(x - 1)",
            "old_expression_latex": "...",
            "new_expression": "(2*x)/1",
            "new_expression_latex": "...",
            "explanation": "0/0 belirsizligi..."
          },
          ...
        ]
      }
    """
    # (Opsiyonel) Eğer premium özelliğin sadece login olmuş kullanıcılara açık olmasını istiyorsak:
    # if "logged_in" not in session or not session["logged_in"]:
    #     return jsonify({"error": "Login required"}), 403

    data = request.get_json()
    expr_str = data.get("expression", "(x^2 - 1)/(x - 1)")
    var_str = data.get("variable", "x")
    limit_pt = data.get("limit_point", 1)

    # Limit hesabı
    result, steps = solve_limit_step_by_step(expr_str, var_str, float(limit_pt))
    # Sonucu string'e dönüştür (ör. "2") ve latex form
    result_str = str(result)
    result_latex = sympy.latex(result)

    # steps içinde "old_expression" ve "new_expression" alanlarını da latex'e çeviriyoruz
    for st in steps:
        old_expr = st.get("old_expression", "")
        new_expr = st.get("new_expression", "")

        try:
            # "Sympy.latex(sympify(...))" => latex string
            old_s = sympy.sympify(old_expr) if old_expr else None
            new_s = sympy.sympify(new_expr) if new_expr else None

            st["old_expression_latex"] = sympy.latex(old_s) if old_s else ""
            st["new_expression_latex"] = sympy.latex(new_s) if new_s else ""
        except Exception:
            # Her ihtimale karşı, latex çevirisinde hata olursa
            st["old_expression_latex"] = ""
            st["new_expression_latex"] = ""

    return jsonify({
        "result": result_str,
        "result_latex": result_latex,
        "steps": steps
    })


if __name__ == "__main__":
    # Örnek: debug modda 5000 portunu dinle
    app.run(debug=True, port=5000)

import React, { useState } from "react";
import axios from "axios";
import StepsViewer from "./StepsViewer";

function App() {
  const [expression, setExpression] = useState("(x^2 - 1)/(x - 1)");
  const [variable, setVariable] = useState("x");
  const [limitPt, setLimitPt] = useState("1");

  const [result, setResult] = useState("");
  const [resultLatex, setResultLatex] = useState("");
  const [steps, setSteps] = useState([]);

  const handleSolve = async () => {
    try {
      const resp = await axios.post("http://127.0.0.1:5000/solve-limit", {
        expression,
        variable,
        limit_point: limitPt
      });
      // API response
      setResult(resp.data.result);           // e.g. "2"
      setResultLatex(resp.data.result_latex); // e.g. "\mathrm{2}"
      setSteps(resp.data.steps);             // array of steps
    } catch (err) {
      console.error(err);
      alert("Error in solve-limit request!");
    }
  };

  return (
    <div style={{ padding: 20, fontFamily: "sans-serif" }}>
      <h1>Limit Solver</h1>
      <div style={{ marginBottom: 10 }}>
        <label>Expression: </label>
        <input
          style={{ width: "250px" }}
          value={expression}
          onChange={(e) => setExpression(e.target.value)}
        />
      </div>
      <div style={{ marginBottom: 10 }}>
        <label>Variable: </label>
        <input
          style={{ width: "50px" }}
          value={variable}
          onChange={(e) => setVariable(e.target.value)}
        />
      </div>
      <div style={{ marginBottom: 10 }}>
        <label>Limit Point: </label>
        <input
          style={{ width: "80px" }}
          value={limitPt}
          onChange={(e) => setLimitPt(e.target.value)}
        />
      </div>

      <button onClick={handleSolve} style={{ marginBottom: 20 }}>
        Solve
      </button>

      <hr />
      <h2>Result: {result}</h2>
      <p>
        LaTeX: <code>{resultLatex}</code>
      </p>

      {/* StepsViewer to list each step, with LaTeX rendering */}
      <StepsViewer steps={steps} />
    </div>
  );
}

export default App;

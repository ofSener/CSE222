import React from "react";
import { MathJax, MathJaxContext } from "better-react-mathjax";

/*
  Example step structure:
  {
    step_number: 1,
    rule: "L'Hopital",
    old_expression: "(x^2 - 1)/(x - 1)",
    new_expression: "(2*x)/1",
    old_expression_latex: "\\frac{x^2 - 1}{x - 1}",
    new_expression_latex: "2x",
    explanation: "0/0 belirsizliği, L'Hopital uygulandı."
  }
*/

function StepsViewer({ steps }) {
  if (!steps || steps.length === 0) {
    return <div>No steps to display.</div>;
  }

  // MathJax configuration (optional)
  const config = {
    loader: { load: ["[tex]/ams"] },
    tex: {
      inlineMath: [["\\(", "\\)"]],
      displayMath: [["\\[", "\\]"]]
    }
  };

  return (
    <MathJaxContext config={config}>
      <h3>Step-by-Step Explanation</h3>
      {steps.map((st) => (
        <div
          key={st.step_number}
          style={{
            border: "1px solid #ccc",
            padding: 10,
            marginBottom: 10,
            borderRadius: 5
          }}
        >
          <p>
            <strong>Step {st.step_number}:</strong> {st.rule || ""}
          </p>

          {/* Old Expression (LaTeX) */}
          <p>
            <strong>Old:</strong>{" "}
            <MathJax inline>{"\\(" + (st.old_expression_latex || "") + "\\)"}</MathJax>
          </p>

          {/* New Expression (LaTeX) */}
          <p>
            <strong>New:</strong>{" "}
            <MathJax inline>{"\\(" + (st.new_expression_latex || "") + "\\)"}</MathJax>
          </p>

          <p>
            <strong>Explanation:</strong> {st.explanation}
          </p>
        </div>
      ))}
    </MathJaxContext>
  );
}

export default StepsViewer;

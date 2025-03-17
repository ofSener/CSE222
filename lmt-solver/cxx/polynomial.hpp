#pragma once

#include <vector>
#include <string>

namespace Poly {

    class Polynomial {
    public:
        Polynomial() = default;
        explicit Polynomial(const std::vector<double>& c)
            : coeffs_(c) {}

        std::vector<double> getCoeffs() const { return coeffs_; }
        Polynomial derivative() const;
        double evaluate(double x) const;
        int degree() const;

    private:
        std::vector<double> coeffs_;
    };

} // namespace Poly

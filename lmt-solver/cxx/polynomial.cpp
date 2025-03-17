#include "polynomial.hpp"
#include <cmath>

namespace Poly {

Polynomial Polynomial::derivative() const {
    if (coeffs_.size() <= 1) {
        return Polynomial({0.0});
    }
    std::vector<double> d;
    d.reserve(coeffs_.size() - 1);
    for (size_t i = 1; i < coeffs_.size(); i++) {
        d.push_back(i * coeffs_[i]);
    }
    return Polynomial(d);
}

double Polynomial::evaluate(double x) const {
    double result = 0.0;
    double xn = 1.0;
    for (size_t i = 0; i < coeffs_.size(); i++) {
        result += coeffs_[i] * xn;
        xn *= x;
    }
    return result;
}

int Polynomial::degree() const {
    for (int i = (int)coeffs_.size() - 1; i >= 0; i--) {
        if (std::fabs(coeffs_[i]) > 1e-14) {
            return i;
        }
    }
    return 0;
}

} // namespace Poly

// PYBIND11 interface
#include <pybind11/pybind11.h>
#include <pybind11/stl.h>

namespace py = pybind11;
using namespace Poly;

PYBIND11_MODULE(polynomial, m) {
    m.doc() = "Optimized polynomial engine in C++ (Pybind11).";

    py::class_<Polynomial>(m, "Polynomial")
        .def(py::init<const std::vector<double>&>(), "Initialize with list of coeffs [a0, a1, ...]")
        .def("getCoeffs", &Polynomial::getCoeffs)
        .def("derivative", &Polynomial::derivative)
        .def("evaluate", &Polynomial::evaluate)
        .def("degree", &Polynomial::degree);
}

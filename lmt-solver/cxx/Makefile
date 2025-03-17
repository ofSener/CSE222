# Makefile

CXX = c++
PYTHON ?= python3
CXXFLAGS = -O3 -Wall -shared -std=c++17 -fPIC

# Pybind11 ve Python extension suffix almak
PYBIND_INCLUDES := $(shell $(PYTHON) -m pybind11 --includes)
PYTHON_EXT := $(shell $(PYTHON) -c 'import sysconfig; print(sysconfig.get_config_var("EXT_SUFFIX"))')

TARGET = polynomial$(PYTHON_EXT)
SRCS = polynomial.cpp

all: $(TARGET)

$(TARGET): $(SRCS) polynomial.hpp
	$(CXX) $(CXXFLAGS) $(PYBIND_INCLUDES) $^ -o $@

clean:
	rm -f $(TARGET)

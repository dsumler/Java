#include <iostream>
#include "Arithmetic.h"
#include "Constant.h"
#include "Expression.h"

template <typename A, typename B>
Expression<A, Arithmetic::plus, B> operator+(const A &l, const B &r)
{
	return Expression<A, Arithmetic::plus, B>(l, r);
};

template <typename A, typename B>
Expression<A, Arithmetic::minus, B> operator-(const A &l, const B &r) {
	return Expression<A, Arithmetic::minus, B>(l, r);
};

template <typename A, typename B>
Expression<A, Arithmetic::multiplication, B> operator*(const A &l, const B &r)
{
	return Expression<A, Arithmetic::multiplication, B>(l, r);
};

template <typename A, typename B>
Expression<A, Arithmetic::division, B> operator/(const A &l, const B &r)
{
	return Expression<A, Arithmetic::division, B>(l, r);
};



int main() {
	std::cout<<"3 * 5 - 3.4 is " << (Constant(3) * Constant(5) * Constant(3.4)).eval();
	return 0;
};

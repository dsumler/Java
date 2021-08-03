#include <iostream>

#ifndef QUESTION3_ARITHMETIC_H
#define QUESTION3_ARITHMETIC_H


class Arithmetic {
public :
    class plus {
    public:
        template<class A, class B>
        inline double static apply(A a, B b) {
            return (a + b);
        }
    };
public :
    class minus {
    public:
        template<class A, class B>
        inline double static apply(A a, B b) {
            return (a - b);
        }
    };
public :
    class multiplication {
    public:
        template<class A, class B>
        inline double static apply(A a, B b) { return (a * b);
        }
    };
public :
    class division {
    public:
        template<class A, class B>
        inline double static apply(A a, B b) {
            return (a / b);
        }
    };

};

template <typename ExprT>
double integrate (ExprT e, double f,double t,size_t n)
{  double sum=0, step=(t-f)/n;
    for (double i=f+step/2; i<t; i+=step)
        sum+=e.eval(i);
    return step*sum;
}

#endif

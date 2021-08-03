//
// Created by dsuml on 14-Jan-20.
//

#ifndef QUESTION3_EXPRESSION_H
#define QUESTION3_EXPRESSION_H


template <typename A, typename BinOp, typename B>
class Expression {
    const A &l_;
    const B &r_;
public:
    Expression(const A &l, const B &r) : l_(l), r_(r)
    {};
    double eval()
    {
        return BinOp::apply(l_, r_);
    };
};


#endif //QUESTION3_EXPRESSION_H

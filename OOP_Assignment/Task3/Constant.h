//
// Created by dsuml on 14-Jan-20.
//

#ifndef QUESTION3_CONSTANT_H
#define QUESTION3_CONSTANT_H


class Constant{
    double n;
public:
    explicit Constant(double n1){
        n = n1;
    }
    double eval(double n1){
        return n1;
    }
};

#endif //QUESTION3_CONSTANT_H

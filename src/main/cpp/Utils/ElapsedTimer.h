//
// Created by bing on 20-1-3.
//

#ifndef CART_ROBOT_ELAPSEDTIMER_H
#define CART_ROBOT_ELAPSEDTIMER_H
#include <time.h>
#include <mutex>

class ElapsedTimer {
public:
    ElapsedTimer(int timeoutVal);

    void restart();

    bool isTimeout();

    void setTimeoutVal(int timeoutVal);

    long getTimeoutVal();

    long elapsed();

    void stop();

    void start();

    static long systemUptime();
private:
    long getSystemUptime();

    long timeoutVal; /// 单位ms
    long startTime;
    bool stopFlag;
    struct timespec tp;
    std::mutex timerMutex;
};


#endif //CART_ROBOT_ELAPSEDTIMER_H

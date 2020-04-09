//
// Created by bing on 20-1-3.
//

#include "ElapsedTimer.h"
#include "time.h"
#include <time.h>

ElapsedTimer::ElapsedTimer(int timeoutVal) :
        timeoutVal(timeoutVal),
        stopFlag(false) {
    this->start();
}

void ElapsedTimer::restart() {
    stopFlag = false;
    start();
}

bool ElapsedTimer::isTimeout() {
    if(stopFlag) {
        return false;
    } else {
        return (getSystemUptime() - startTime) > timeoutVal;
    }

}

void ElapsedTimer::setTimeoutVal(int timeoutVal) {
    restart();
    timerMutex.lock();
    this->timeoutVal = timeoutVal;
    timerMutex.unlock();
}

long ElapsedTimer::getTimeoutVal() {
    std::lock_guard<std::mutex> locker(timerMutex);
    return timeoutVal;
}

long ElapsedTimer::elapsed() {
    return getSystemUptime() - startTime;
}

void ElapsedTimer::stop() {
    stopFlag = true;
}

void ElapsedTimer::start() {
    timerMutex.lock();
    startTime = getSystemUptime();
    timerMutex.unlock();
}

long ElapsedTimer::getSystemUptime()
{
    clock_gettime(CLOCK_MONOTONIC, &tp);
    return (long)tp.tv_sec * 1000 + tp.tv_nsec / 1000000;
}

long ElapsedTimer::systemUptime()
{
    struct timespec tp;
    clock_gettime(CLOCK_MONOTONIC, &tp);
    return (long)tp.tv_sec * 1000 + tp.tv_nsec / 1000000;
}
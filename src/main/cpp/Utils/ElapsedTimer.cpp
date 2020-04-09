//
// Created by bing on 20-1-3.
//

#include "ElapsedTimer.h"
#include "time.h"
#include <time.h>
#ifdef _WIN32
#include <windows.h>
#pragma comment( lib,"winmm.lib" )
#elif
#endif

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
	if (stopFlag) {
		return false;
	}
	else {
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
#ifdef _WIN32
	return timeGetTime();
#elif
	clock_gettime(CLOCK_MONOTONIC, &tp);
	return (long)tp.tv_sec * 1000 + tp.tv_nsec / 1000000;
#endif
}

long ElapsedTimer::systemUptime()
{
#ifdef _WIN32
	return timeGetTime();
#elif
	struct timespec tp;
	clock_gettime(CLOCK_MONOTONIC, &tp);
	return (long)tp.tv_sec * 1000 + tp.tv_nsec / 1000000;
#endif
}

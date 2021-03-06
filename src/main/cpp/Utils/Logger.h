//
// Created by bing on 20-1-2.
//

#ifndef CAMPORT3_AS_SAMPLE_LOGGER_H
#define CAMPORT3_AS_SAMPLE_LOGGER_H

#include <memory>
#include <string>
#include <fstream>
#include <mutex>

using std::ofstream;
using namespace std;

enum LoggerLevel {
	LOGGER_LEVEL_DEBUG = 0,
	LOGGER_LEVEL_INFO,
	LOGGER_LEVEL_WARN,
	LOGGER_LEVEL_ERROR,
};

class Logger {
public:
	/**
	*
	* @param name name of files, format name-datetime(mm_dd_hhmmss).csv
	* @param reserveFileNum
	*/
	Logger(string logModuleName, int reserveFileNum);

	/**
	* get instance
	* @return
	*/
	static std::shared_ptr<Logger>& instance();

	/**
	* write log to file
	* @param level
	* @param log
	* @param fileName
	* @param lineNum
	*/
	void logToFile(LoggerLevel level, string log, string fileName, int lineNum);

	void logToFile(string level, string log, string fileName, int lineNum);

	void logToFile(string level, string logTag, string log, string fileName, int lineNum);

	string getTimeString();

	string getName();

	static string printLog(const char* format, ...);
private:
	void checkLineNum();

	void checkFileNum();

	void writeLogToFile(string text);

	string logModuleName;
	string softLinkName;
	int reserveFileNum;
	int lineNumber;
	ofstream olog;
	std::mutex mutex;
};

//#define LOG_DEBUG(...) Logger::instance()->logToFile("DEBUG",Logger::printLog(__VA_ARGS__), __FILE__,__LINE__);
//#define LOG_INFO(...) Logger::instance()->logToFile("INFO",Logger::printLog(__VA_ARGS__), __FILE__,__LINE__);
//#define LOG_WARN(...) Logger::instance()->logToFile("WARN",Logger::printLog(__VA_ARGS__), __FILE__,__LINE__);
//#define LOG_ERROR(...) Logger::instance()->logToFile("ERROR",Logger::printLog(__VA_ARGS__), __FILE__,__LINE__);
#ifdef _WIN32
#include <Windows.h>
#define LOG_DEBUG(...) {\
                        char buf[1024]; \
                        _snprintf(buf, sizeof(buf) - 1, __VA_ARGS__);\
                        Logger::instance()->logToFile("DEBUG",buf,__FILE__,__LINE__);}

#define LOG_INFO(...) {\
                        char buf[1024]; \
                        _snprintf(buf, sizeof(buf) - 1, __VA_ARGS__);\
                        Logger::instance()->logToFile("INFO",buf,__FILE__,__LINE__);}

#define LOG_WARN(...) {\
                        char buf[1024]; \
                        _snprintf(buf, sizeof(buf) - 1, __VA_ARGS__);\
                        Logger::instance()->logToFile("WARN",buf,__FILE__,__LINE__);}

#define LOG_ERROR(...) {\
                        char buf[1024]; \
                        _snprintf(buf, sizeof(buf) - 1, __VA_ARGS__);\
                        Logger::instance()->logToFile("ERROR",buf,__FILE__,__LINE__);}
#else
#define LOG_DEBUG(...) {\
                        char buf[1024]; \
                        snprintf(buf, sizeof(buf) - 1, __VA_ARGS__);\
                        Logger::instance()->logToFile("DEBUG",buf, __FILE__,__LINE__);}

#define LOG_INFO(...) {\
                        char buf[1024]; \
                        snprintf(buf, sizeof(buf) - 1, __VA_ARGS__);\
                        Logger::instance()->logToFile("INFO",buf, __FILE__,__LINE__);}

#define LOG_WARN(...) {\
                        char buf[1024]; \
                        snprintf(buf, sizeof(buf) - 1, __VA_ARGS__);\
                        Logger::instance()->logToFile("WARN",buf, __FILE__,__LINE__);}

#define LOG_ERROR(...) {\
                        char buf[1024]; \
                        snprintf(buf, sizeof(buf) - 1, __VA_ARGS__);\
                        Logger::instance()->logToFile("ERROR",buf, __FILE__,__LINE__);}
#endif

#endif //CAMPORT3_AS_SAMPLE_LOGGER_H

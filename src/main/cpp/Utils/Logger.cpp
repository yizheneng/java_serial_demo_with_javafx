//
// Created by bing on 20-1-2.
//

#include "Logger.h"
#include <sys/stat.h>
#include <sys/types.h>
#include <stdio.h>

#ifdef _WIN32
#include <direct.h>
#include <windows.h>
#else
#include <dirent.h>
#include <jni.h>
#include <sys/time.h>
#include <string.h>
#endif

#ifdef __ANDROID__
#define LOG_DIR "/sdcard/control_log/"
#else
#define LOG_DIR "./control_log/"
#endif
#define KEEP_LOG_FILE_TIME (20 * 24 * 60 * 60)

std::shared_ptr<Logger>& Logger::instance()
{
	static std::shared_ptr<Logger> instance = std::make_shared<Logger>("control", 20);
	return instance;
}

Logger::Logger(string logModuleName, int reserveFileNum) :
logModuleName(logModuleName),
reserveFileNum(reserveFileNum),
lineNumber(0)
{
#ifdef _WIN32
    if(0 == _mkdir(LOG_DIR)) {
	        printf("Succeed!\r\n");
	} else {
	        char* s = strerror(errno);
	        printf("Error:%s\r\n", s);
    }

	string name = getName();
	printf("name:%s\r\n", name.c_str());
	olog.open(name, std::ios::app);
#else
	if (0 == mkdir(LOG_DIR, S_IRWXU)) {
		printf("Succeed!\r\n");
	}
	else {
		char* s = strerror(errno);
		printf("Error:%s\r\n", s);
	}

	softLinkName = LOG_DIR + logModuleName + "_latest";
	string name = getName();
	olog.open(name, std::ios::app);

//	system(("rm " + softLinkName).c_str());
//	string linkCmd = "ln -s \"" + name + "\" " + softLinkName;
//	system(linkCmd.c_str());
#endif

	checkFileNum();
}

std::string Logger::getTimeString()
{
#ifdef _WIN32
	SYSTEMTIME sys;
	GetLocalTime(&sys);
	char tmp[100] = { 0 };
	sprintf(tmp, "%4d-%02d-%02d %02d_%02d_%02d.%03d", sys.wYear, sys.wMonth,  sys.wDay, sys.wHour, sys.wMinute, sys.wSecond,sys.wMilliseconds);
	return std::string(tmp);
#else
	struct timeval tv;
	gettimeofday(&tv, NULL);

	struct tm* tmP = localtime(&tv.tv_sec);
	char buf[40];
	strftime(buf,
		sizeof(buf) - 1,
		"%Y-%m-%d %H_%M_%S",
		tmP);
	return string(buf) + "." + to_string(tv.tv_usec / 1000);
#endif
}

std::string Logger::getName()
{
	string name = LOG_DIR + logModuleName + "_" + getTimeString() + ".csv";
	return name;
}

void Logger::logToFile(LoggerLevel level, string log, string fileName, int lineNum)
{
	string levelStr;
	switch (level) {
	case LOGGER_LEVEL_DEBUG:
		levelStr = string("DEBUG") + ",";
		break;
	case LOGGER_LEVEL_INFO:
		levelStr = string("INFO") + ",";
		break;
	case LOGGER_LEVEL_WARN:
		levelStr = string("WARN") + ",";
		break;
	case LOGGER_LEVEL_ERROR:
		levelStr = string("ERROR") + ",";
		break;
	default:
		break;
	}

	levelStr = levelStr
		+ getTimeString()
		+ ","
		+ log
		+ ","
		+ fileName
		+ ":"
		+ to_string(lineNum);

	writeLogToFile(levelStr);
}

void Logger::logToFile(string level, string log, string fileName, int lineNum)
{
	writeLogToFile(level
		+ ","
		+ getTimeString()
		+ ","
		+ log
		+ ","
		+ fileName
		+ ":"
		+ to_string(lineNum));
}

void Logger::logToFile(string level, string logTag, string log, string fileName, int lineNum)
{
	writeLogToFile(level
		+ ","
		+ getTimeString()
		+ ","
		+ logTag
		+ ","
		+ log
		+ ","
		+ fileName
		+ ":"
		+ to_string(lineNum));
}

string Logger::printLog(const char* format, ...)
{
	char buf[1024];
	va_list vaList;
	va_start(vaList, format);
#ifdef _WIN32
	_snprintf(buf, sizeof(buf) - 1, format, vaList);
#else
	snprintf(buf, sizeof(buf) - 1, format, vaList);
#endif
	va_end(vaList);
	return string(buf);
}

void Logger::checkLineNum()
{
	lineNumber++;
	if (lineNumber >= 2000) {
		lineNumber = 0;
		olog.close();
		checkFileNum();
		string name = getName();
		olog.open(name, std::ios::app);
#ifdef _WIN32

#else
		system(("rm " + softLinkName).c_str());
		string linkCmd = "ln -s " + name + " " + softLinkName;
		system(linkCmd.c_str());
#endif
	}
}

void Logger::checkFileNum()
{
#ifdef _WIN32

#else
	DIR* dir = opendir(LOG_DIR);
	if (dir == NULL) {
		return;
	}

	struct timeval tv;
	gettimeofday(&tv, NULL);

	if (tv.tv_sec < 1577956854) {
		// ERROR:time error!
		return;
	}

	struct dirent * filename;
	int fd;
	struct stat buf;
	FILE * fp;
	char filePath[200];
	while ((filename = readdir(dir)) != NULL)
	{
		if (strcmp(filename->d_name, ".") == 0 ||
			strcmp(filename->d_name, "..") == 0)
			continue;

		snprintf(filePath, sizeof(filePath) - 1, "%s%s", LOG_DIR, filename->d_name);
		fp = fopen(filePath, "r");
		if (NULL != fp)
		{
			fd = fileno(fp);
			fstat(fd, &buf);
			long modify_time = buf.st_mtime; //latest modification time (seconds passed from 01/01/00:00:00 1970 UTC)
			fclose(fp);

			if ((tv.tv_sec - modify_time) > KEEP_LOG_FILE_TIME) {
				remove(filePath);
			}
		}
	}
#endif
}

void Logger::writeLogToFile(string text) {
	printf("%s\r\n", text.c_str());
	mutex.lock();
	olog << text << endl;
	checkLineNum();
	mutex.unlock();
}

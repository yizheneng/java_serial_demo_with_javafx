//
// Created by bing on 20-1-7.
//

#include "StringUtils.h"

#define MAX_STRING_SIZE 1024

string StringUtils::charArrayToHexString(const char *dataBuf, const uint16_t dataLength) {
    char buf[1024 * 3 + 1];

    if(NULL == dataBuf) {
        return "";
    }

    if(dataLength >  1024) {
        return "Data out of range!";
    }

    for(int i = 0; i < dataLength; i++) {
        getHexChar(buf + i*3, dataBuf[i]);
    }

    buf[dataLength * 3] = '\0';

    return std::string(buf);
}

void StringUtils::getHexChar(char *buf, const char data) {
    uint8_t f = (uint8_t)data >> 4 & (uint8_t)0x0f;
    uint8_t b = (uint8_t)data & (uint8_t)0x0f;

    if(f <= 9) {
        buf[0] = (uint8_t)0x30 + f;
    } else {
        buf[0] = f - (uint8_t)9 + (uint8_t)0x40;
    }

    if(b <= 9) {
        buf[1] = (uint8_t)0x30 + b;
    } else {
        buf[1] = b - (uint8_t)9 + (uint8_t)0x40;
    }

    buf[2] = ' ';
}

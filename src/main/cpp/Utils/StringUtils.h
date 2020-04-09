//
// Created by bing on 20-1-7.
//

#ifndef CART_ROBOT_STRINGUTILS_H
#define CART_ROBOT_STRINGUTILS_H

#include <string>
#include <stdint.h>

using namespace std;

class StringUtils {
public:
    static void getHexChar(char* buf, const char data);
    static string charArrayToHexString(const char* dataBuf, const uint16_t dataLength);
};


#endif //CART_ROBOT_STRINGUTILS_H

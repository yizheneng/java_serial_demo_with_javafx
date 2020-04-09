cmake_minimum_required(VERSION 3.4.1)
add_compile_options(-std=c++11)

include_directories(
        ${CMAKE_SOURCE_DIR}/Utils/
        {$JAVA_HOME}/include/
        /home/bing/Downloads/idea-IC-193.6911.18/jbr/include
        /home/bing/Downloads/idea-IC-193.6911.18/jbr/include/linux
)

set(PROJECT_SRCS
        Utils/Logger.cpp
        Utils/ElapsedTimer.cpp
        Utils/StringUtils.cpp
        yizheneng_Utils_JniLogger.cpp
)

add_library(
    native-lib

    SHARED

    ${PROJECT_SRCS}
)
target_link_libraries(
    native-lib
    )

set(CMAKE_BUILD_TYPE Debug)
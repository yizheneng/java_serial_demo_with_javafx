cmake_minimum_required(VERSION 3.4.1)
add_compile_options(-std=c++11)

include_directories(
        /home/bing/idea/jbr/include
        /home/bing/idea/jbr/include/linux

        ${CMAKE_SOURCE_DIR}/Utils/
        {CMAKE_SOURCE_DIR}/Driver/Serial/impl/list_ports/
        ${CMAKE_SOURCE_DIR}/Driver/Serial/impl/
        ${CMAKE_SOURCE_DIR}/Driver/Serial/
)

set(PROJECT_SRCS
        Utils/Logger.cpp
        Utils/ElapsedTimer.cpp
        Utils/StringUtils.cpp
        yizheneng_Utils_JniLogger.cpp
        yizheneng_Driver_SerialPort.cpp

        Driver/Serial/serial.cc
        Driver/Serial/impl/unix.cc
        Driver/Serial/impl/list_ports/list_ports_linux.cc
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
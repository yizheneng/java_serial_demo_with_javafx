cmake_minimum_required(VERSION 3.4.1)
add_compile_options(-std=c++11)

project(jni_lib)

IF (CMAKE_SYSTEM_NAME MATCHES "Linux")
    set (JniIncludePath
        /home/bing/idea/jbr/include
        /home/bing/idea/jbr/include/linux
    )

    set (SerialSources
        Driver/Serial/serial.cc
        Driver/Serial/impl/unix.cc
        Driver/Serial/impl/list_ports/list_ports_linux.cc
    )
ELSE ()
    set (JniIncludePath
            "C:/Program Files/JetBrains/IntelliJ IDEA Community Edition 2019.3.4/jbr/include"
            "C:/Program Files/JetBrains/IntelliJ IDEA Community Edition 2019.3.4/jbr/include/win32"
            "C:/Program Files/JetBrains/IntelliJ IDEA Community Edition 2019.3.4/jbr/include/win32/bridge"
        )

    set (SerialSources
            Driver/Serial/serial.cc
            Driver/Serial/impl/win.cc
            Driver/Serial/impl/list_ports/list_ports_win.cc
        )
ENDIF (CMAKE_SYSTEM_NAME MATCHES "Linux")

include_directories(
        ${JniIncludePath}
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
)

IF(CMAKE_CL_64)
    set(SerialLibName
        nativeSerial_x64-lib
    )
ELSE(CMAKE_CL_64)
    set(SerialLibName
            nativeSerial_x86-lib
    )
ENDIF(CMAKE_CL_64)

add_library(
    ${SerialLibName}

    SHARED

    ${PROJECT_SRCS}
    ${SerialSources}
)

target_link_libraries(
    ${SerialLibName}
)

set(CMAKE_BUILD_TYPE Debug)
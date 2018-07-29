package com.enrico200165.utils.config;

public class Exception_YAMLCfg_WrongType extends Exception {

    public Exception_YAMLCfg_WrongType(String ex_type, String foundType ,String k) {
        this.key = k;
        this.expected_type = ex_type;
        this.found_type = foundType;
    }

    public String toString() { return "wrong type in YAMLfor key: "+key+" expected: "+expected_type+" found: "+found_type; }

    public String expected_type;
    public String found_type;
    public String key;
}

package com.elytradev.betterboilers.util;

public interface IConfigSerializable {
    String toConfigString();
    boolean matches(String configName);
}
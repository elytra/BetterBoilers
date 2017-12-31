package com.elytradev.betterboilers.util;

public interface IConfigSerializable {
    public String toConfigString();
    public boolean matches(String configName);
}
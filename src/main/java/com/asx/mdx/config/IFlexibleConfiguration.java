package com.asx.mdx.config;

import java.util.ArrayList;

public interface IFlexibleConfiguration
{
    public ArrayList<ConfigSetting> allSettings();
    
    public void saveSettings();
}

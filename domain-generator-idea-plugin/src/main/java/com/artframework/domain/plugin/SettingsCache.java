package com.artframework.domain.plugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author li.pengcheng
 * @version V1.0
 * @date 2024/1/19
 **/
@State(name = "settings.cache", storages = {@Storage(value = "ddd-settings-cache.xml")})
public class SettingsCache implements PersistentStateComponent<SettingsCache> {

    public Map<String, String> cacheMap = new LinkedHashMap<>();

    // 获取反序列化后的结果-项目级别
    public static SettingsCache getInstance(Project project) {
        return project.getService(SettingsCache.class);
    }

    @Override
    public @Nullable SettingsCache getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull SettingsCache state) {
        if (state == null) {
            return;
        }
        // 拿到反序列化后的对象数据
        this.cacheMap = state.cacheMap;
    }
}
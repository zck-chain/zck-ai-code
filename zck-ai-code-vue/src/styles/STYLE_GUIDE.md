# 样式指南与主题API文档

## 1. 设计系统概述

本项目采用现代化的设计系统，基于CSS变量和组件化架构，提供统一的视觉体验和灵活的主题定制能力。设计系统包含以下核心模块：

- **颜色系统**：定义了一套完整的颜色变量，包括主色调、功能色和中性色
- **排版系统**：标准化的字体大小、字重和行高
- **间距系统**：统一的间距单位，确保页面布局的一致性
- **组件样式**：标准化的组件外观和交互效果
- **响应式设计**：适配不同屏幕尺寸的布局策略
- **主题架构**：支持亮色和暗色主题的切换

## 2. 颜色系统

### 2.1 主色调

| 变量名 | 描述 | 默认值 |
|-------|------|-------|
| `--primary-color` | 主色调 | `#1890ff` |
| `--primary-hover` | 主色调悬停状态 | `#40a9ff` |
| `--primary-active` | 主色调激活状态 | `#096dd9` |

### 2.2 功能色

| 变量名 | 描述 | 默认值 |
|-------|------|-------|
| `--success-color` | 成功色 | `#52c41a` |
| `--warning-color` | 警告色 | `#faad14` |
| `--error-color` | 错误色 | `#f5222d` |
| `--info-color` | 信息色 | `#1890ff` |

### 2.3 中性色

| 变量名 | 描述 | 默认值 |
|-------|------|-------|
| `--text-primary` | 主要文本色 | `#333333` |
| `--text-secondary` | 次要文本色 | `#666666` |
| `--text-tertiary` |  tertiary文本色 | `#999999` |
| `--border-color` | 边框颜色 | `#f0f0f0` |
| `--border-light` | 浅色边框 | `#f5f5f5` |
| `--background-light` | 浅色背景 | `#fafafa` |
| `--background-default` | 默认背景 | `#ffffff` |
| `--background-page` | 页面背景 | `#f0f2f5` |

## 3. 排版系统

### 3.1 字体家族

```css
--font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
```

### 3.2 字体大小

| 变量名 | 描述 | 默认值 |
|-------|------|-------|
| `--font-size-xs` | 极小字体 | `12px` |
| `--font-size-sm` | 小字体 | `14px` |
| `--font-size-md` | 中字体 | `16px` |
| `--font-size-lg` | 大字体 | `18px` |
| `--font-size-xl` | 特大字体 | `20px` |
| `--font-size-xxl` | 超大字体 | `24px` |
| `--font-size-xxxl` | 最大字体 | `28px` |

### 3.3 字体权重

| 变量名 | 描述 | 默认值 |
|-------|------|-------|
| `--font-weight-normal` | 正常字重 | `400` |
| `--font-weight-medium` | 中等字重 | `500` |
| `--font-weight-semibold` | 半粗体 | `600` |
| `--font-weight-bold` | 粗体 | `700` |

## 4. 间距系统

| 变量名 | 描述 | 默认值 |
|-------|------|-------|
| `--spacing-xs` | 极小间距 | `4px` |
| `--spacing-sm` | 小间距 | `8px` |
| `--spacing-md` | 中间距 | `16px` |
| `--spacing-lg` | 大间距 | `24px` |
| `--spacing-xl` | 特大间距 | `32px` |
| `--spacing-xxl` | 超大间距 | `48px` |

## 5. 阴影系统

| 变量名 | 描述 | 默认值 |
|-------|------|-------|
| `--shadow-sm` | 小阴影 | `0 1px 2px rgba(0, 0, 0, 0.05)` |
| `--shadow-md` | 中阴影 | `0 2px 8px rgba(0, 0, 0, 0.08)` |
| `--shadow-lg` | 大阴影 | `0 4px 12px rgba(0, 0, 0, 0.15)` |
| `--shadow-xl` | 特大阴影 | `0 8px 24px rgba(0, 0, 0, 0.2)` |

## 6. 过渡动画

| 变量名 | 描述 | 默认值 |
|-------|------|-------|
| `--transition-fast` | 快速过渡 | `0.2s ease` |
| `--transition-normal` | 正常过渡 | `0.3s ease` |
| `--transition-slow` | 慢速过渡 | `0.5s ease` |

## 7. 布局尺寸

| 变量名 | 描述 | 默认值 |
|-------|------|-------|
| `--header-height` | 头部高度 | `64px` |
| `--footer-height` | 底部高度 | `70px` |
| `--content-min-height` | 内容最小高度 | `calc(100vh - var(--header-height) - var(--footer-height))` |
| `--max-container-width` | 最大容器宽度 | `1200px` |

## 8. 响应式断点

| 变量名 | 描述 | 默认值 |
|-------|------|-------|
| `--breakpoint-xs` | 极小屏幕 | `480px` |
| `--breakpoint-sm` | 小屏幕 | `576px` |
| `--breakpoint-md` | 中等屏幕 | `768px` |
| `--breakpoint-lg` | 大屏幕 | `992px` |
| `--breakpoint-xl` | 特大屏幕 | `1200px` |
| `--breakpoint-xxl` | 超大屏幕 | `1600px` |

## 9. 主题API文档

### 9.1 主题管理器

主题管理器是一个工具类，用于管理和切换应用的主题。

#### 9.1.1 导入主题管理器

```typescript
import { themeManager } from '@/utils/themeManager';
```

#### 9.1.2 核心方法

| 方法名 | 描述 | 参数 | 返回值 |
|-------|------|------|-------|
| `applyTheme` | 应用指定主题 | `theme: ThemeType | string` 主题类型或自定义主题名称 | `void` |
| `getCurrentTheme` | 获取当前主题 | 无 | `ThemeType` 当前主题类型 |
| `toggleTheme` | 切换主题 | 无 | `ThemeType` 切换后的主题类型 |
| `onThemeChange` | 注册主题变化监听器 | `listener: (theme: ThemeType) => void` 监听器函数 | `() => void` 取消监听的函数 |
| `registerTheme` | 注册自定义主题 | `name: string` 主题名称<br>`config: ThemeConfig` 主题配置 | `void` |
| `getAvailableThemes` | 获取所有可用主题 | 无 | `string[]` 主题名称列表 |
| `resetToDefault` | 重置为默认主题 | 无 | `void` |

### 9.2 主题配置接口

```typescript
interface ThemeConfig {
  type: ThemeType;          // 主题类型：'light' 或 'dark'
  variables: Record<string, string>;  // 主题变量对象
}

type ThemeType = 'light' | 'dark';
```

### 9.3 使用示例

#### 9.3.1 切换主题

```typescript
// 切换主题
const newTheme = themeManager.toggleTheme();
console.log('Current theme:', newTheme);
```

#### 9.3.2 应用指定主题

```typescript
// 应用亮色主题
themeManager.applyTheme('light');

// 应用暗色主题
themeManager.applyTheme('dark');
```

#### 9.3.3 监听主题变化

```typescript
// 注册主题变化监听器
const unsubscribe = themeManager.onThemeChange((theme) => {
  console.log('Theme changed to:', theme);
  // 在这里可以执行主题变化相关的逻辑
});

// 取消监听
// unsubscribe();
```

#### 9.3.4 注册自定义主题

```typescript
// 注册自定义主题
themeManager.registerTheme('custom', {
  type: 'light',
  variables: {
    '--primary-color': '#722ed1',
    '--primary-hover': '#9254de',
    '--primary-active': '#531dab',
    // 其他主题变量...
  }
});

// 应用自定义主题
themeManager.applyTheme('custom');
```

## 10. 组件样式规范

### 10.1 通用组件样式

所有组件都应使用CSS变量来定义样式，确保与主题系统的兼容性。例如：

```vue
<template>
  <div class="custom-component">
    <h2 class="component-title">组件标题</h2>
    <p class="component-text">组件文本</p>
    <button class="component-button">组件按钮</button>
  </div>
</template>

<style scoped>
.custom-component {
  background: var(--background-default);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-md);
  padding: var(--spacing-lg);
}

.component-title {
  font-size: var(--font-size-xl);
  font-weight: var(--font-weight-semibold);
  color: var(--text-primary);
  margin-bottom: var(--spacing-md);
}

.component-text {
  font-size: var(--font-size-sm);
  color: var(--text-secondary);
  margin-bottom: var(--spacing-lg);
}

.component-button {
  background: var(--primary-color);
  color: white;
  border: none;
  padding: var(--spacing-sm) var(--spacing-md);
  border-radius: var(--border-radius-md);
  font-size: var(--font-size-sm);
  cursor: pointer;
  transition: all var(--transition-normal);
}

.component-button:hover {
  background: var(--primary-hover);
  box-shadow: var(--shadow-sm);
}
</style>
```

### 10.2 响应式设计

组件应使用媒体查询和相对单位来实现响应式设计，例如：

```css
/* 响应式设计 */
@media (max-width: var(--breakpoint-md)) {
  .custom-component {
    padding: var(--spacing-md);
  }
  
  .component-title {
    font-size: var(--font-size-lg);
  }
}

@media (max-width: var(--breakpoint-xs)) {
  .custom-component {
    padding: var(--spacing-sm);
  }
}
```

## 11. 最佳实践

### 11.1 样式使用规范

1. **优先使用CSS变量**：所有样式值都应使用CSS变量，避免硬编码
2. **保持组件隔离**：使用`scoped`样式或CSS Modules确保组件样式隔离
3. **遵循设计系统**：严格按照设计系统的规范实现组件样式
4. **考虑主题兼容性**：确保所有组件在不同主题下都能正常显示
5. **优化性能**：避免过度使用复杂的CSS选择器和动画

### 11.2 主题定制建议

1. **扩展默认主题**：基于默认主题进行定制，保持设计的一致性
2. **合理命名**：为自定义主题使用清晰、描述性的名称
3. **完整覆盖**：确保自定义主题覆盖所有必要的变量
4. **测试验证**：在不同主题下测试组件的显示效果
5. **文档化**：为自定义主题提供清晰的文档说明

## 12. 故障排除

### 12.1 常见问题

| 问题 | 可能原因 | 解决方案 |
|-----|---------|--------|
| 主题切换不生效 | CSS变量未正确应用 | 检查组件是否使用了CSS变量 |
| 暗色主题显示异常 | 某些变量未在暗色主题中定义 | 确保所有使用的变量都在暗色主题中有对应的值 |
| 响应式布局失效 | 媒体查询使用了固定值而非变量 | 使用`var(--breakpoint-*)`变量 |
| 性能问题 | 过度使用复杂动画或选择器 | 简化CSS，减少不必要的动画 |

### 12.2 调试技巧

1. **浏览器开发者工具**：使用浏览器的开发者工具检查CSS变量的值
2. **主题切换测试**：在开发过程中频繁切换主题，确保兼容性
3. **响应式测试**：使用浏览器的设备模拟功能测试不同屏幕尺寸
4. **性能分析**：使用浏览器的性能分析工具检查CSS性能

## 13. 总结

本设计系统和主题架构为项目提供了以下优势：

- **一致性**：统一的视觉语言和交互体验
- **灵活性**：支持多种主题的定制和切换
- **可维护性**：模块化的样式结构，易于维护和扩展
- **性能优化**：高效的CSS变量和组件化设计
- **未来兼容性**：为未来的功能扩展和设计变更提供了坚实的基础

通过遵循本指南，开发者可以确保应用的视觉一致性和用户体验的质量，同时为未来的样式定制和主题扩展做好准备。
// 主题类型定义
export type ThemeType = 'light' | 'dark';

// 主题配置接口
export interface ThemeConfig {
  type: ThemeType;
  variables: Record<string, string>;
}

// 默认主题配置
const defaultThemes: Record<ThemeType, ThemeConfig> = {
  light: {
    type: 'light',
    variables: {
      '--primary-color': '#1890ff',
      '--primary-hover': '#40a9ff',
      '--primary-active': '#096dd9',
      '--success-color': '#52c41a',
      '--warning-color': '#faad14',
      '--error-color': '#f5222d',
      '--info-color': '#1890ff',
      '--text-primary': '#333333',
      '--text-secondary': '#666666',
      '--text-tertiary': '#999999',
      '--border-color': '#f0f0f0',
      '--border-light': '#f5f5f5',
      '--background-light': '#fafafa',
      '--background-default': '#ffffff',
      '--background-page': '#f0f2f5',
      '--shadow-sm': '0 1px 2px rgba(0, 0, 0, 0.05)',
      '--shadow-md': '0 2px 8px rgba(0, 0, 0, 0.08)',
      '--shadow-lg': '0 4px 12px rgba(0, 0, 0, 0.15)',
      '--shadow-xl': '0 8px 24px rgba(0, 0, 0, 0.2)'
    }
  },
  dark: {
    type: 'dark',
    variables: {
      '--primary-color': '#1890ff',
      '--primary-hover': '#40a9ff',
      '--primary-active': '#096dd9',
      '--success-color': '#52c41a',
      '--warning-color': '#faad14',
      '--error-color': '#f5222d',
      '--info-color': '#1890ff',
      '--text-primary': '#ffffff',
      '--text-secondary': '#e0e0e0',
      '--text-tertiary': '#b0b0b0',
      '--border-color': '#434343',
      '--border-light': '#333333',
      '--background-light': '#1f1f1f',
      '--background-default': '#141414',
      '--background-page': '#0f0f0f',
      '--shadow-sm': '0 1px 2px rgba(0, 0, 0, 0.3)',
      '--shadow-md': '0 2px 8px rgba(0, 0, 0, 0.4)',
      '--shadow-lg': '0 4px 12px rgba(0, 0, 0, 0.5)',
      '--shadow-xl': '0 8px 24px rgba(0, 0, 0, 0.6)'
    }
  }
};

// 主题管理器类
class ThemeManager {
  private currentTheme: ThemeType;
  private customThemes: Record<string, ThemeConfig> = {};
  private themeChangeListeners: Array<(theme: ThemeType) => void> = [];

  constructor() {
    // 从本地存储加载主题，默认使用亮色主题
    const savedTheme = localStorage.getItem('theme') as ThemeType | null;
    this.currentTheme = savedTheme || 'light';
    this.applyTheme(this.currentTheme);
  }

  /**
   * 应用主题
   * @param theme 主题类型或自定义主题名称
   */
  public applyTheme(theme: ThemeType | string): void {
    const themeConfig = this.customThemes[theme] || defaultThemes[theme as ThemeType];

    if (!themeConfig) {
      console.warn(`Theme ${theme} not found, using default light theme`);
      this.applyTheme('light');
      return;
    }

    // 更新HTML元素的data-theme属性
    document.documentElement.setAttribute('data-theme', themeConfig.type);

    // 应用主题变量
    this.applyThemeVariables(themeConfig.variables);

    // 更新当前主题
    this.currentTheme = themeConfig.type;

    // 保存主题到本地存储
    localStorage.setItem('theme', this.currentTheme);

    // 触发主题变化监听器
    this.themeChangeListeners.forEach(listener => listener(this.currentTheme));
  }

  /**
   * 应用主题变量
   * @param variables 主题变量对象
   */
  private applyThemeVariables(variables: Record<string, string>): void {
    const root = document.documentElement;

    // 清除现有的主题变量
    Object.keys(variables).forEach(variable => {
      root.style.removeProperty(variable);
    });

    // 应用新的主题变量
    Object.entries(variables).forEach(([key, value]) => {
      root.style.setProperty(key, value);
    });
  }

  /**
   * 获取当前主题
   * @returns 当前主题类型
   */
  public getCurrentTheme(): ThemeType {
    return this.currentTheme;
  }

  /**
   * 切换主题
   * @returns 切换后的主题类型
   */
  public toggleTheme(): ThemeType {
    const newTheme: ThemeType = this.currentTheme === 'light' ? 'dark' : 'light';
    this.applyTheme(newTheme);
    return newTheme;
  }

  /**
   * 注册主题变化监听器
   * @param listener 监听器函数
   * @returns 取消监听的函数
   */
  public onThemeChange(listener: (theme: ThemeType) => void): () => void {
    this.themeChangeListeners.push(listener);

    // 返回取消监听的函数
    return () => {
      this.themeChangeListeners = this.themeChangeListeners.filter(l => l !== listener);
    };
  }

  /**
   * 注册自定义主题
   * @param name 主题名称
   * @param config 主题配置
   */
  public registerTheme(name: string, config: ThemeConfig): void {
    this.customThemes[name] = config;
  }

  /**
   * 获取所有可用主题
   * @returns 主题名称列表
   */
  public getAvailableThemes(): string[] {
    return [...Object.keys(defaultThemes), ...Object.keys(this.customThemes)];
  }

  /**
   * 重置为默认主题
   */
  public resetToDefault(): void {
    this.applyTheme('light');
  }
}

// 导出单例实例
export const themeManager = new ThemeManager();

// 导出主题管理器类（如果需要创建多个实例）
export default ThemeManager;

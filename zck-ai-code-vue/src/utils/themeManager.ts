// 主题类型定义
export type ThemeType = 'light' | 'dark'

// 主题配置接口
export interface ThemeConfig {
  id: string
  name: string
  type: ThemeType
  colors: Record<string, string>
}

// 主题变量映射
const THEME_VARIABLES: Record<string, string> = {
  '--primary-color': '--theme-primary',
  '--primary-hover': '--theme-primary-hover',
  '--primary-active': '--theme-primary-active',
  '--success-color': '--theme-success',
  '--warning-color': '--theme-warning',
  '--error-color': '--theme-error',
  '--info-color': '--theme-info',
  '--text-primary': '--theme-text-primary',
  '--text-secondary': '--theme-text-secondary',
  '--text-tertiary': '--theme-text-tertiary',
  '--border-color': '--theme-border',
  '--border-light': '--theme-border-light',
  '--background-light': '--theme-bg-light',
  '--background-default': '--theme-bg-default',
  '--background-page': '--theme-bg-page',
  '--shadow-sm': '--theme-shadow-sm',
  '--shadow-md': '--theme-shadow-md',
  '--shadow-lg': '--theme-shadow-lg',
  '--shadow-xl': '--theme-shadow-xl',
}

// 默认主题定义
const DEFAULT_THEMES: Record<ThemeType, ThemeConfig> = {
  light: {
    id: 'light',
    name: '亮色主题',
    type: 'light',
    colors: {
      '--theme-primary': '#1890ff',
      '--theme-primary-hover': '#40a9ff',
      '--theme-primary-active': '#096dd9',
      '--theme-success': '#52c41a',
      '--theme-warning': '#faad14',
      '--theme-error': '#f5222d',
      '--theme-info': '#1890ff',
      '--theme-text-primary': '#333333',
      '--theme-text-secondary': '#666666',
      '--theme-text-tertiary': '#999999',
      '--theme-border': '#f0f0f0',
      '--theme-border-light': '#f5f5f5',
      '--theme-bg-light': '#fafafa',
      '--theme-bg-default': '#ffffff',
      '--theme-bg-page': '#f0f2f5',
      '--theme-shadow-sm': '0 1px 2px rgba(0, 0, 0, 0.05)',
      '--theme-shadow-md': '0 2px 8px rgba(0, 0, 0, 0.08)',
      '--theme-shadow-lg': '0 4px 12px rgba(0, 0, 0, 0.15)',
      '--theme-shadow-xl': '0 8px 24px rgba(0, 0, 0, 0.2)',
    },
  },
  dark: {
    id: 'dark',
    name: '暗色主题',
    type: 'dark',
    colors: {
      '--theme-primary': '#1890ff',
      '--theme-primary-hover': '#40a9ff',
      '--theme-primary-active': '#096dd9',
      '--theme-success': '#52c41a',
      '--theme-warning': '#faad14',
      '--theme-error': '#f5222d',
      '--theme-info': '#1890ff',
      '--theme-text-primary': '#ffffff',
      '--theme-text-secondary': '#e0e0e0',
      '--theme-text-tertiary': '#b0b0b0',
      '--theme-border': '#434343',
      '--theme-border-light': '#333333',
      '--theme-bg-light': '#1f1f1f',
      '--theme-bg-default': '#141414',
      '--theme-bg-page': '#0f0f0f',
      '--theme-shadow-sm': '0 1px 2px rgba(0, 0, 0, 0.3)',
      '--theme-shadow-md': '0 2px 8px rgba(0, 0, 0, 0.4)',
      '--theme-shadow-lg': '0 4px 12px rgba(0, 0, 0, 0.5)',
      '--theme-shadow-xl': '0 8px 24px rgba(0, 0, 0, 0.6)',
    },
  },
}

// 主题管理器类
class ThemeManager {
  private currentTheme: ThemeType
  private customThemes: Map<string, ThemeConfig> = new Map()
  private listeners: Set<(theme: ThemeType) => void> = new Set()
  private rootElement: HTMLElement

  constructor() {
    this.rootElement = document.documentElement
    const savedTheme = localStorage.getItem('theme') as ThemeType | null
    this.currentTheme = savedTheme || 'light'
    this.applyTheme(this.currentTheme)
  }

  private applyThemeVariables(theme: ThemeConfig): void {
    Object.entries(theme.colors).forEach(([key, value]) => {
      this.rootElement.style.setProperty(key, value)
    })
  }

  private removeThemeVariables(theme: ThemeConfig): void {
    Object.keys(theme.colors).forEach(key => {
      this.rootElement.style.removeProperty(key)
    })
  }

  public applyTheme(themeId: ThemeType | string): void {
    const theme = this.customThemes.get(themeId) || DEFAULT_THEMES[themeId as ThemeType]

    if (!theme) {
      console.warn(`Theme ${themeId} not found, falling back to light theme`)
      this.applyTheme('light')
      return
    }

    const oldTheme = DEFAULT_THEMES[this.currentTheme]
    if (oldTheme) {
      this.removeThemeVariables(oldTheme)
    }

    this.applyThemeVariables(theme)
    this.rootElement.setAttribute('data-theme', theme.type)
    this.currentTheme = theme.type
    localStorage.setItem('theme', theme.type)

    this.listeners.forEach(listener => listener(theme.type))
  }

  public getCurrentTheme(): ThemeType {
    return this.currentTheme
  }

  public toggleTheme(): ThemeType {
    const newTheme: ThemeType = this.currentTheme === 'light' ? 'dark' : 'light'
    this.applyTheme(newTheme)
    return newTheme
  }

  public onThemeChange(listener: (theme: ThemeType) => void): () => void {
    this.listeners.add(listener)
    return () => {
      this.listeners.delete(listener)
    }
  }

  public registerTheme(theme: ThemeConfig): void {
    this.customThemes.set(theme.id, theme)
  }

  public unregisterTheme(themeId: string): boolean {
    return this.customThemes.delete(themeId)
  }

  public getAvailableThemes(): Array<{ id: string; name: string; type: ThemeType }> {
    const themes: Array<{ id: string; name: string; type: ThemeType }> = [
      { id: 'light', name: DEFAULT_THEMES.light.name, type: 'light' },
      { id: 'dark', name: DEFAULT_THEMES.dark.name, type: 'dark' },
    ]

    this.customThemes.forEach(theme => {
      themes.push({ id: theme.id, name: theme.name, type: theme.type })
    })

    return themes
  }

  public isDarkMode(): boolean {
    return this.currentTheme === 'dark'
  }

  public resetToDefault(): void {
    this.applyTheme('light')
  }
}

export const themeManager = new ThemeManager()
export default ThemeManager

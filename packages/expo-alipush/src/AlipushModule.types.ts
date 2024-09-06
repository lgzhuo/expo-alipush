import type { ProxyNativeModule } from "expo-modules-core";

export interface CustomNotificationConfig {
  isBuildWhenAppInForeground?: boolean;
  isServerOptionFirst?: boolean;
  remindType?: number;
  notificationFlags?: number;
}

export interface AlipushModule extends ProxyNativeModule {
  init?: () => Promise<void>;

  register?: (deviceToken: string) => Promise<void>;

  getDeviceId?: () => string;

  bindAccount?: (account: string) => Promise<void>;

  unbindAccount?: () => Promise<void>;

  setCustomNotificationConfig?: (
    id: number,
    config: CustomNotificationConfig
  ) => Promise<void>;

  
  checkPushChannelStatus?: () => Promise<"on" | "off">;

 
  turnOnPushChannel?: () => Promise<void>;

  
  turnOffPushChannel?: () => Promise<void>;
}

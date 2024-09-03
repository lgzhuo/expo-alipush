import type { ProxyNativeModule } from "expo-modules-core";

export interface CustomNotificationConfig {
  isBuildWhenAppInForeground?: boolean;
  isServerOptionFirst?: boolean;
  remindType?: number;
  notificationFlags?: number;
}

export interface AlipushModule extends ProxyNativeModule {
  /**
   * init alipush, should called before register
   *
   * @platform ios
   */
  init?: () => Promise<void>;

  /**
   * register alipush
   *
   * @param deviceToken required in ios
   * @platform android, ios
   */
  register?: (deviceToken?: string) => Promise<void>;

  /**
   * get the device id generate by alipush
   *
   * @platform android, ios
   */
  getDeviceId?: () => string;

  /**
   * bind account to alipush
   *
   * @param account
   * @platform android, ios
   */
  bindAccount?: (account: string) => Promise<void>;

  /**
   * unbind account
   *
   * @platform android, ios
   */
  unbindAccount?: () => Promise<void>;

  /**
   * config custom notification
   * @see https://help.aliyun.com/document_detail/2834944.html
   *
   * @param id
   * @param config
   *
   * @platform android
   */
  setCustomNotificationConfig?: (
    id: number,
    config: CustomNotificationConfig
  ) => Promise<void>;

  /**
   * get current status of push channel
   *
   * @returns status of push channel, will be enum "on" or "off"
   *
   * @platform android
   */
  checkPushChannelStatus?: () => Promise<"on" | "off">;

  /**
   * turn on alipush channel
   * 
   * @platform android
   */
  turnOnPushChannel?: () => Promise<void>;

  /**
   * turn off alipush channel
   * 
   * @platform android
   */
  turnOffPushChannel?: () => Promise<void>
}

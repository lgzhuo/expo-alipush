import { ProxyNativeModule } from "expo-modules-core";
import type {
  AlipushForegroundNotification,
  AlipushNotification,
  Notification,
  NotificationResponse,
} from "./Notification.types";

export type AlipushTriggeredNotification = Notification & {
  request: {
    trigger: {
      type: "push";
      alipushNotification: AlipushNotification | AlipushForegroundNotification;
    };
  };
};

export type AlipushTriggeredNotificationResponse = NotificationResponse & {
  notification: AlipushTriggeredNotification;
};

export interface AlipushNotificationEmitterModule extends ProxyNativeModule {
  getLastAlipushNotificationResponse?: () => Promise<AlipushTriggeredNotificationResponse | null>;
}

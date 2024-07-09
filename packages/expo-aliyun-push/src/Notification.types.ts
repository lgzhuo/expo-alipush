import type * as ExpoNotifications from "expo-notifications";

interface BaseAliyunNotification {
  title: string | null;
  summary: string | null;
  extra: Record<string, string>;
  date: number;
  isForeground: boolean;
}

export interface AliyunNotification extends BaseAliyunNotification {
  isForeground: false;
}

export interface AliyunForegroundNotification extends BaseAliyunNotification {
  isForeground: true;
  openType: number;
  openActivity?: string;
  openUrl?: number;
}

export interface PushNotificationTrigger
  extends ExpoNotifications.PushNotificationTrigger {
  /**
   * when triggered by aliyun-push in android
   */
  aliyunNotification?: AliyunNotification | AliyunForegroundNotification;
}

export type NotificationTrigger =
  | Exclude<ExpoNotifications.NotificationTrigger, { type: "push" }>
  | PushNotificationTrigger;

export interface NotificationRequest
  extends Omit<ExpoNotifications.NotificationRequest, "trigger"> {
  trigger: NotificationTrigger;
}

export interface Notification
  extends Omit<ExpoNotifications.Notification, "request"> {
  request: NotificationRequest;
}

export interface NotificationResponse
  extends Omit<ExpoNotifications.NotificationResponse, "notification"> {
  notification: Notification;
}

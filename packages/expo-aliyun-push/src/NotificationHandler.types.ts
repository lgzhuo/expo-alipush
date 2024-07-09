import type * as ExpoNotifications from "expo-notifications";
import type { Notification } from "./Notification.types";

export interface NotificationHandler
  extends Omit<ExpoNotifications.NotificationHandler, "handleNotification"> {
  handleNotification: (
    notification: Notification
  ) => Promise<ExpoNotifications.NotificationBehavior>;
}

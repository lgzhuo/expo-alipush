import {
  setNotificationHandler as setExpoNotificationHandler,
  type Subscription,
} from "expo-notifications";
import { addAlipushNotificationReceivedListener } from "./AlipushNotificationEmitter";
import type { Notification } from "./Notification.types";
import type { NotificationHandler } from "./NotificationHandler.types";

const alipushNotificationMap = new Map<string, Notification>();
let notificationSubscription: Subscription | null = null;

export function setNotificationHandler(handler: NotificationHandler | null) {
  if (handler) {
    notificationSubscription ??= addAlipushNotificationReceivedListener(
      (notification) => {
        if (notification.request.trigger.alipushNotification.isForeground) {
          // store the foreground notification, this will used later in handler
          alipushNotificationMap.set(
            notification.request.identifier,
            notification
          );
        }
      }
    );
    setExpoNotificationHandler({
      ...handler,
      handleNotification(notification) {
        // expo-notifications emit the notification with trigger type 'unknown'
        // we replace it with correctly one
        const id = notification.request.identifier;
        if (
          notification.request.trigger.type === "unknown" &&
          alipushNotificationMap.has(id)
        ) {
          notification = alipushNotificationMap.get(id)!;
          alipushNotificationMap.delete(id);
        }
        return handler.handleNotification(notification);
      },
    });
  } else {
    notificationSubscription?.remove();
    notificationSubscription = null;
    setExpoNotificationHandler(null);
  }
}

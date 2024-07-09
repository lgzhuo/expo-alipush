import {
  setNotificationHandler as setExpoNotificationHandler,
  type Subscription,
} from "expo-notifications";
import { addAliyunNotificationReceivedListener } from "./AliyunNotificationEmitter";
import type { Notification } from "./Notification.types";
import type { NotificationHandler } from "./NotificationHandler.types";

const aliyunNotificationMap = new Map<string, Notification>();
let notificationSubscription: Subscription | null = null;

export function setNotificationHandler(handler: NotificationHandler | null) {
  if (handler) {
    notificationSubscription ??= addAliyunNotificationReceivedListener(
      (notification) => {
        if (notification.request.trigger.aliyunNotification.isForeground) {
          // store the foreground notification, this will used later in handler
          aliyunNotificationMap.set(
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
          aliyunNotificationMap.has(id)
        ) {
          notification = aliyunNotificationMap.get(id)!;
          aliyunNotificationMap.delete(id);
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

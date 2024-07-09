import { EventEmitter, Subscription } from "expo-modules-core";
import AlipushNotificationEmitterModule from "./AlipushNotificationEmitterModule";
import type {
  AlipushForegroundNotification,
  AlipushNotification,
  Notification,
  NotificationResponse,
} from "./Notification.types";

const emitter = new EventEmitter(AlipushNotificationEmitterModule);

const didReceiveNotificationEventName = "onDidReceiveAlipushNotification";
const didReceiveNotificationResponseEventName =
  "onDidReceiveAlipushNotificationResponse";

type AlipushTriggeredNotification = Notification & {
  request: {
    trigger: {
      type: "push";
      alipushNotification: AlipushNotification | AlipushForegroundNotification;
    };
  };
};

type AlipushTriggeredNotificationResponse = NotificationResponse & {
  notification: AlipushTriggeredNotification;
};

export function addAlipushNotificationReceivedListener(
  listener: (event: AlipushTriggeredNotification) => void
): Subscription {
  return emitter.addListener(didReceiveNotificationEventName, listener);
}

export function addAlipushNotificationResponseReceivedListener(
  listener: (event: AlipushTriggeredNotificationResponse) => void
): Subscription {
  return emitter.addListener(didReceiveNotificationResponseEventName, listener);
}

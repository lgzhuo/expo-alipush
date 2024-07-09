import { EventEmitter, Subscription } from "expo-modules-core";
import AliyunNotificationEmitterModule from "./AliyunNotificationEmitterModule";
import type {
  AliyunForegroundNotification,
  AliyunNotification,
  Notification,
  NotificationResponse,
} from "./Notification.types";

const emitter = new EventEmitter(AliyunNotificationEmitterModule);

const didReceiveNotificationEventName = "onDidReceiveAliyunNotification";
const didReceiveNotificationResponseEventName =
  "onDidReceiveAliyunNotificationResponse";

type AliyunTriggeredNotification = Notification & {
  request: {
    trigger: {
      type: "push";
      aliyunNotification: AliyunNotification | AliyunForegroundNotification;
    };
  };
};

type AliyunTriggeredNotificationResponse = NotificationResponse & {
  notification: AliyunTriggeredNotification;
};

export function addAliyunNotificationReceivedListener(
  listener: (event: AliyunTriggeredNotification) => void
): Subscription {
  return emitter.addListener(didReceiveNotificationEventName, listener);
}

export function addAliyunNotificationResponseReceivedListener(
  listener: (event: AliyunTriggeredNotificationResponse) => void
): Subscription {
  return emitter.addListener(didReceiveNotificationResponseEventName, listener);
}

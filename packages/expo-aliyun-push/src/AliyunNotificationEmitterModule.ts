import { Platform } from "expo-modules-core";

import { AliyunNotificationEmitterModule } from "./AliyunNotificationEmitterModule.types";

let warningHasBeenShown = false;

export default {
  addListener: () => {
    if (!warningHasBeenShown) {
      console.warn(
        `[expo-aliyun-push] Emitting notifications is not yet fully supported on ${Platform.OS}. Adding a listener will have no effect.`
      );
      warningHasBeenShown = true;
    }
  },
  removeListeners: () => {},
} satisfies AliyunNotificationEmitterModule;

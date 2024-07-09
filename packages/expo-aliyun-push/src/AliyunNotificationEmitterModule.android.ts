import { requireNativeModule } from "expo-modules-core";

import { AliyunNotificationEmitterModule } from "./AliyunNotificationEmitterModule.types";

export default requireNativeModule<AliyunNotificationEmitterModule>(
  "AliyunNotificationEmitter"
);

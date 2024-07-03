import * as React from 'react';

import { AliyunPushViewProps } from './AliyunPush.types';

export default function AliyunPushView(props: AliyunPushViewProps) {
  return (
    <div>
      <span>{props.name}</span>
    </div>
  );
}

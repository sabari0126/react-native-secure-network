#import <React/RCTBridgeModule.h>
#import <React/RCTEventEmitter.h>

#ifdef RCT_NEW_ARCH_ENABLED

#import "SecureNetworkSpec.h"
@interface SecureNetwork : NSObject <NativeSecureNetworkSpec>
@end

#else

@interface SecureNetwork : RCTEventEmitter <RCTBridgeModule>
@end

#endif
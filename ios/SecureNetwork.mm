#import "SecureNetwork.h"
#import "InsecureNetworkDetection.h"

@implementation SecureNetwork

// We won't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeSecureNetworkSpecJSI>(params);
}
#endif

RCT_EXPORT_MODULE()

RCT_EXPORT_METHOD(getConnectionStatus: (RCTPromiseResolveBlock)resolve
                  rejecter:(RCTPromiseRejectBlock)reject)
{
  dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0), ^{
    NetworkSecurityResult *result =
        [[InsecureNetworkDetection shared] checkNetworkSecurity];

    if (!result) {
      resolve(@{
        @"isSecureNetwork": @YES,
        @"message": @"Unable to determine network security status"
      });
      return;
    }

    resolve(@{
      @"isSecureNetwork": @(result.isSecureNetwork),
      @"message": result.message ?: @""
    });
  });
}

@end

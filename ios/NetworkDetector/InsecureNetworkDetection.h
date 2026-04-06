#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NetworkSecurityResult : NSObject

@property (nonatomic, assign) BOOL isSecureNetwork;
@property (nonatomic, strong) NSString *message;
@property (nonatomic, assign) NSInteger code;

- (instancetype)initWithIsSecureNetwork:(BOOL)isSecureNetwork message:(NSString *)message code:(NSInteger)code;

@end

@interface InsecureNetworkDetection : NSObject

+ (instancetype)shared;
- (nullable NetworkSecurityResult *)checkNetworkSecurity;

@end

NS_ASSUME_NONNULL_END

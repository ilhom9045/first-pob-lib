                Pod::Spec.new do |spec|
    spec.name                     = 'ZOffice'
    spec.version                  = '1.0.0'
    spec.homepage                 = 'Link to the Shared Module homepage'
    spec.source                   = { :http=> ''}
    spec.authors                  = ''
    spec.license                  = ''
    spec.summary                  = 'Some description for the Shared Module'
    spec.vendored_frameworks      = 'cocoapods/release/1.0.0/shared.xcframework'
    spec.libraries                = 'c++'
    spec.ios.deployment_target = '12.0'
         
end
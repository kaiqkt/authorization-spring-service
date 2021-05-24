package authorizationservice.domain.entities

import com.fasterxml.jackson.annotation.JsonProperty


data class DeviceDetails(
    @JsonProperty("Useragent")
    var userAgent: String? = null,
    @JsonProperty("DeviceClass")
    var deviceClass: String? = null,
    @JsonProperty("DeviceName")
    var deviceName: String? = null,
    @JsonProperty("DeviceBrand")
    var deviceBrand: String? = null,
    @JsonProperty("OperatingSystemClass")
    var operatingSystemClass: String? = null,
    @JsonProperty("OperatingSystemName")
    var operatingSystemName: String? = null,
    @JsonProperty("OperatingSystemVersion")
    var operatingSystemVersion: String? = null,
    @JsonProperty("OperatingSystemVersionMajor")
    var operatingSystemVersionMajor: String? = null,
    @JsonProperty("OperatingSystemNameVersion")
    var operatingSystemNameVersion: String? = null,
    @JsonProperty("OperatingSystemNameVersionMajor")
    var operatingSystemNameVersionMajor: String? = null,
    @JsonProperty("OperatingSystemVersionBuild")
    var operatingSystemVersionBuild: String? = null,
    @JsonProperty("LayoutEngineClass")
    var layoutEngineClass: String? = null,
    @JsonProperty("LayoutEngineName")
    var layoutEngineName: String? = null,
    @JsonProperty("LayoutEngineVersion")
    var layoutEngineVersion: String? = null,
    @JsonProperty("LayoutEngineVersionMajor")
    var layoutEngineVersionMajor: String? = null,
    @JsonProperty("LayoutEngineNameVersion")
    var layoutEngineNameVersion: String? = null,
    @JsonProperty("LayoutEngineNameVersionMajor")
    var layoutEngineNameVersionMajor: String? = null,
    @JsonProperty("AgentClass")
    var agentClass: String? = null,
    @JsonProperty("AgentName")
    var agentName: String? = null,
    @JsonProperty("AgentVersion")
    var agentVersion: String? = null,
    @JsonProperty("AgentVersionMajor")
    var agentVersionMajor: String? = null,
    @JsonProperty("AgentNameVersion")
    var agentNameVersion: String? = null,
    @JsonProperty("AgentNameVersionMajor")
    var agentNameVersionMajor: String? = null,
)

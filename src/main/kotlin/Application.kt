package de.hska

import de.hska.config.Settings.banner
import de.hska.config.Settings.props
import org.springframework.boot.WebApplicationType.REACTIVE
import org.springframework.boot.actuate.autoconfigure.audit.AuditEventsEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.cache.CachesEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.condition.ConditionsReportEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.health.HealthContributorAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.health.HealthEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.integration.IntegrationGraphEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.logging.LogFileWebEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.logging.LoggersEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.management.HeapDumpWebEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.management.ThreadDumpEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.CompositeMeterRegistryAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.JvmMetricsAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.KafkaMetricsAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.LogbackMetricsAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.SystemMetricsAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.export.simple.SimpleMetricsExportAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.metrics.web.tomcat.TomcatMetricsAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.mongo.MongoHealthContributorAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.mongo.MongoReactiveHealthContributorAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.scheduling.ScheduledTasksEndpointAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.system.DiskSpaceHealthContributorAutoConfiguration
import org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceEndpointAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
import org.springframework.boot.autoconfigure.dao.PersistenceExceptionTranslationAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoReactiveRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcDataAutoConfiguration
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcRepositoriesAutoConfiguration
import org.springframework.boot.autoconfigure.data.r2dbc.R2dbcTransactionManagerAutoConfiguration
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration
import org.springframework.boot.autoconfigure.hateoas.HypermediaAutoConfiguration
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
import org.springframework.boot.autoconfigure.mongo.MongoReactiveAutoConfiguration
import org.springframework.boot.autoconfigure.r2dbc.ConnectionFactoryAutoConfiguration
import org.springframework.boot.autoconfigure.security.oauth2.resource.reactive.ReactiveOAuth2ResourceServerAutoConfiguration
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration
import org.springframework.boot.autoconfigure.transaction.TransactionAutoConfiguration
import org.springframework.boot.autoconfigure.web.client.RestTemplateAutoConfiguration
import org.springframework.boot.autoconfigure.web.embedded.EmbeddedWebServerFactoryCustomizerAutoConfiguration
import org.springframework.boot.autoconfigure.web.reactive.error.ErrorWebFluxAutoConfiguration
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.context.ApplicationPidFileWriter
import org.springframework.boot.runApplication
import org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration
import org.springframework.cloud.autoconfigure.LifecycleMvcEndpointAutoConfiguration
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration
import org.springframework.cloud.autoconfigure.RefreshEndpointAutoConfiguration
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JAutoConfiguration
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JAutoConfiguration
import org.springframework.cloud.client.CommonsClientAutoConfiguration
import org.springframework.cloud.client.ReactiveCommonsClientAutoConfiguration
import org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClientAutoConfiguration
import org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClientAutoConfiguration
import org.springframework.cloud.client.loadbalancer.AsyncLoadBalancerAutoConfiguration
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerClientAutoConfiguration
import org.springframework.cloud.client.serviceregistry.ServiceRegistryAutoConfiguration
import org.springframework.cloud.config.client.ConfigClientAutoConfiguration
import org.springframework.cloud.configuration.CompatibilityVerifierAutoConfiguration
import org.springframework.cloud.loadbalancer.config.BlockingLoadBalancerClientAutoConfiguration
import org.springframework.cloud.loadbalancer.config.LoadBalancerAutoConfiguration
import org.springframework.cloud.loadbalancer.config.LoadBalancerCacheAutoConfiguration
import org.springframework.cloud.sleuth.instrument.async.AsyncAutoConfiguration
import org.springframework.cloud.sleuth.instrument.reactor.TraceReactorAutoConfiguration
import org.springframework.cloud.sleuth.instrument.rxjava.RxJavaAutoConfiguration
import org.springframework.cloud.stream.config.BindersHealthIndicatorAutoConfiguration
import org.springframework.cloud.stream.config.BindingsEndpointAutoConfiguration
import org.springframework.cloud.stream.config.ChannelBindingAutoConfiguration
import org.springframework.cloud.stream.config.ChannelsEndpointAutoConfiguration
import reactor.tools.agent.ReactorDebugAgent

@SpringBootApplication(
    exclude = [
        AopAutoConfiguration::class,
        AuditEventsEndpointAutoConfiguration::class,
        CachesEndpointAutoConfiguration::class,
        CompositeMeterRegistryAutoConfiguration::class,
        ConditionsReportEndpointAutoConfiguration::class,
        DiskSpaceHealthContributorAutoConfiguration::class,
        EmbeddedWebServerFactoryCustomizerAutoConfiguration::class,
        ErrorMvcAutoConfiguration::class,
        ErrorWebFluxAutoConfiguration::class,
        GsonAutoConfiguration::class,
        HealthContributorAutoConfiguration::class,
        HealthEndpointAutoConfiguration::class,
        HeapDumpWebEndpointAutoConfiguration::class,
        HttpTraceEndpointAutoConfiguration::class,
        HypermediaAutoConfiguration::class,
        JvmMetricsAutoConfiguration::class,
        KafkaAutoConfiguration::class,
        KafkaMetricsAutoConfiguration::class,
        LifecycleMvcEndpointAutoConfiguration::class,
        LogbackMetricsAutoConfiguration::class,
        LogFileWebEndpointAutoConfiguration::class,
        LoggersEndpointAutoConfiguration::class,
        MetricsAutoConfiguration::class,
        MetricsEndpointAutoConfiguration::class,
        MongoDataAutoConfiguration::class,
        MongoHealthContributorAutoConfiguration::class,
        MongoReactiveDataAutoConfiguration::class,
        MongoReactiveHealthContributorAutoConfiguration::class,
        MongoReactiveRepositoriesAutoConfiguration::class,
        MongoRepositoriesAutoConfiguration::class,
        PersistenceExceptionTranslationAutoConfiguration::class,
        ReactiveOAuth2ResourceServerAutoConfiguration::class,
        RefreshEndpointAutoConfiguration::class,
        RestTemplateAutoConfiguration::class,
        RxJavaAutoConfiguration::class,
        SimpleMetricsExportAutoConfiguration::class,
        ScheduledTasksEndpointAutoConfiguration::class,
        SystemMetricsAutoConfiguration::class,
        TaskExecutionAutoConfiguration::class,
        TaskSchedulingAutoConfiguration::class,
        ThreadDumpEndpointAutoConfiguration::class,
        TomcatMetricsAutoConfiguration::class,
        WebClientAutoConfiguration::class,
        WebMvcAutoConfiguration::class,

        AsyncAutoConfiguration::class,
        AsyncLoadBalancerAutoConfiguration::class,
        BindersHealthIndicatorAutoConfiguration::class,
        BindingsEndpointAutoConfiguration::class,
        BlockingLoadBalancerClientAutoConfiguration::class,
        CacheAutoConfiguration::class,
        ChannelBindingAutoConfiguration::class,
        ChannelsEndpointAutoConfiguration::class,
        CommonsClientAutoConfiguration::class,
        CompatibilityVerifierAutoConfiguration::class,
        CompositeDiscoveryClientAutoConfiguration::class,
        ConfigClientAutoConfiguration::class,
        ConfigurationPropertiesRebinderAutoConfiguration::class,
        ConnectionFactoryAutoConfiguration::class,
        IntegrationGraphEndpointAutoConfiguration::class,
        LoadBalancerAutoConfiguration::class,
        org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration::class,
        LoadBalancerCacheAutoConfiguration::class,
        MongoAutoConfiguration::class,
        MongoReactiveAutoConfiguration::class,
        R2dbcDataAutoConfiguration::class,
        R2dbcRepositoriesAutoConfiguration::class,
        R2dbcTransactionManagerAutoConfiguration::class,
        ReactiveCommonsClientAutoConfiguration::class,
        ReactiveResilience4JAutoConfiguration::class,
        ReactorLoadBalancerClientAutoConfiguration::class,
        RefreshAutoConfiguration::class,
        Resilience4JAutoConfiguration::class,
        ServiceRegistryAutoConfiguration::class,
        SimpleDiscoveryClientAutoConfiguration::class,
        TraceReactorAutoConfiguration::class,
        TransactionAutoConfiguration::class
    ]
)
class Application

/**
 * Hauptprogramm, um den Microservice zu starten.
 *
 * @param args Evtl. zusätzliche Argumente für den Start des Microservice
 */
fun main(args: Array<String>) {
    ReactorDebugAgent.init()
    // BlockHound.install()

    @Suppress("SpreadOperator")
    runApplication<Application>(*args) {
        webApplicationType = REACTIVE
        setBanner(banner)
        setDefaultProperties(props)
        addListeners(ApplicationPidFileWriter())
    }
}

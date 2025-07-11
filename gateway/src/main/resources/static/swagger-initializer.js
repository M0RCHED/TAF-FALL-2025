window.onload = function() {
  window.ui = SwaggerUIBundle({
    urls: [
      { url: "/service-a/v3/api-docs", name: "Service A" },
      { url: "/service-b/v3/api-docs", name: "Service B" }
    ],
    dom_id: '#swagger-ui',
    deepLinking: true,
    presets: [SwaggerUIBundle.presets.apis, SwaggerUIStandalonePreset],
    layout: "StandaloneLayout"
  });
};

package io.sudostream.classtimetablereader

trait MiniKubeHelper {

  val mongoKeystorePassword = try {
    sys.env("MONGODB_KEYSTORE_PASSWORD")
  } catch {
    case e: Exception => ""
  }

  val isMinikubeRun: Boolean = try {
    if (sys.env("MINIKUBE_RUN") == "true") {
      true
    } else {
      false
    }
  }
  catch {
    case e: Exception => false
  }

  if (isMinikubeRun) {
    println("MINIKUBE = yes")
    System.setProperty("javax.net.ssl.keyStore", "/etc/ssl/cacerts")
    System.setProperty("javax.net.ssl.keyStorePassword", mongoKeystorePassword)
    System.setProperty("javax.net.ssl.trustStore", "/etc/ssl/cacerts")
    System.setProperty("javax.net.ssl.trustStorePassword", mongoKeystorePassword)
  }
}

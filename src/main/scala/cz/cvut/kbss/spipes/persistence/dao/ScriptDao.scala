package cz.cvut.kbss.spipes.persistence.dao

import java.io.File
import java.net.URI
import java.util.{List => JList}

import cz.cvut.kbss.jopa.Persistence
import cz.cvut.kbss.jopa.model._
import cz.cvut.kbss.ontodriver.jena.config.JenaOntoDriverProperties
import cz.cvut.kbss.spipes.model.AbstractEntity
import cz.cvut.kbss.spipes.model.Vocabulary._
import cz.cvut.kbss.spipes.model.spipes.{Module, ModuleType}
import cz.cvut.kbss.spipes.util._
import javax.annotation.PostConstruct
import org.apache.jena.rdf.model.{Model, ModelFactory}
import org.springframework.stereotype.Repository

import scala.collection.JavaConverters._
import scala.util.Try

/**
  * Created by Miroslav Blasko on 2.1.17.
  */
@Repository
class ScriptDao extends PropertySource with Logger[ScriptDao] with ResourceManager with ScriptManager {

  var emf: EntityManagerFactory = _

  @PostConstruct
  def init: Unit = {
    // create persistence unit
    val props: Map[String, String] = Map(
      // Here we set up basic storage access properties - driver class, physical location of the storage
      JOPAPersistenceProperties.ONTOLOGY_PHYSICAL_URI_KEY -> "local://temporary", // jopa uses the URI scheme to choose between local and remote repo, file and (http, https and ftp)resp.
      JOPAPersistenceProperties.ONTOLOGY_URI_KEY -> "http://temporary",
      JOPAPersistenceProperties.DATA_SOURCE_CLASS -> "cz.cvut.kbss.ontodriver.jena.JenaDataSource",
      // Ontology language
      JOPAPersistenceProperties.LANG -> Constants.PU_LANGUAGE,
      // Where to look for entity classes
      JOPAPersistenceProperties.SCAN_PACKAGE -> "cz.cvut.kbss.spipes.model",
      // Persistence provider name
      PersistenceProperties.JPA_PERSISTENCE_PROVIDER -> classOf[JOPAPersistenceProvider].getName(),
      JenaOntoDriverProperties.IN_MEMORY -> true.toString())
    emf = Persistence.createEntityManagerFactory("testPersistenceUnit", props.asJava)
  }

  def getModules(m: Model): Try[JList[Module]] =
    get(m)(s_c_Modules)(classOf[Module])

  def getModuleTypes(m: Model): Try[JList[ModuleType]] =
    get(m)(s_c_Module)(classOf[ModuleType])


  private def get[T <: AbstractEntity](m: Model) =
    (owlClass: String) =>
      (resultClass: Class[T]) => Try {
        val em = emf.createEntityManager()
        val inferredModel = ModelFactory.createRDFSModel(m)
        val dataset = JopaPersistenceUtils.getDataset(em)
        dataset.setDefaultModel(inferredModel)
        emf.getCache().evict(resultClass)
        val query = em.createNativeQuery("select ?s where { ?s a ?type }", resultClass)
          .setParameter("type", URI.create(owlClass))
        query.getResultList()
      }

  def getScripts: Option[Set[File]] = {
    val scriptsPaths = discoverLocations
    log.info("Looking for any scripts in " + scriptsPaths.mkString("[", ",", "]"))
    scriptsPaths.toSet.flatMap((f: File) => find(f, Set())) match {
      case i if i.nonEmpty => Some(i)
      case _ => None
    }
  }

  def getScriptsWithImports(ignore: Boolean): Option[Set[(File, Set[File])]] = {
    val scriptsPaths = discoverLocations
    log.info("Looking for any scripts in " + scriptsPaths.mkString("[", ",", "]"))
    scriptsPaths.toSet.map((f: File) => f -> find(f, Set(), ignore)).filter(_._2.nonEmpty) match {
      case i if i.nonEmpty =>
        Some(i)
      case _ => None
    }
  }

  private def find(root: File, acc: Set[File], ignore: Boolean = false): Set[File] =
    if (ignore && ignored.contains(root)) {
      log.info(f"""Ignoring $root""")
      acc
    }
    else {
      if (root.isFile() && root.getName().contains(".ttl"))
        acc + root
      else if (root.isDirectory())
        root.listFiles() match {
          case s if s.nonEmpty => s.map((f) => find(f, acc)).reduceLeft(_ ++ _)
          case _ => acc
        }
      else
        acc
    }
}
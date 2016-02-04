package br.com.sicot.test;

import br.com.sicot.model.Categoria;
import br.com.sicot.service.CategoriaRegistration;
import br.com.sicot.util.Resources;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.logging.Logger;

import static org.junit.Assert.assertNotNull;

@RunWith(Arquillian.class)
public class MemberRegistrationTest {
   @Deployment
   public static Archive<?> createTestArchive() {
      return ShrinkWrap.create(WebArchive.class, "test.war")
            .addClasses(Categoria.class, CategoriaRegistration.class, Resources.class)
            .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
            .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
            // Deploy our test datasource
            .addAsWebInfResource("test-ds.xml", "test-ds.xml");
   }

   @Inject
   CategoriaRegistration categoriaRegistration;

   @Inject
   Logger log;

   @Test
   public void testRegister() throws Exception {
      Categoria newCategoria = new Categoria();
      newCategoria.setNome("Jane Doe");
      categoriaRegistration.register(newCategoria);
      assertNotNull(newCategoria.getId());
      log.info(newCategoria.getNome() + " was persisted with id " + newCategoria.getId());
   }
   
}

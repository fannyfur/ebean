package io.ebeaninternal.server.deploy;

import io.ebean.bean.BeanCollection;
import io.ebean.bean.BeanCollectionLoader;
import io.ebean.bean.EntityBean;
import io.ebeaninternal.server.text.json.SpiJsonWriter;

import java.io.IOException;
import java.util.Collection;

abstract class BaseCollectionHelp<T> implements BeanCollectionHelp<T> {

  final BeanPropertyAssocMany<T> many;
  final BeanDescriptor<T> targetDescriptor;
  final String propertyName;

  BeanCollectionLoader loader;

  BaseCollectionHelp(BeanPropertyAssocMany<T> many) {
    this.many = many;
    this.targetDescriptor = many.getTargetDescriptor();
    this.propertyName = many.getName();
  }

   BaseCollectionHelp() {
    this.many = null;
    this.targetDescriptor = null;
    this.propertyName = null;
  }

  @Override
  public void setLoader(BeanCollectionLoader loader) {
    this.loader = loader;
  }

  @Override
  public void add(BeanCollection<?> collection, EntityBean bean, boolean withCheck) {
    if (withCheck) {
      collection.internalAddWithCheck(bean);
    } else {
      collection.internalAdd(bean);
    }
  }

  @Override
  public Collection underlying(Object value) {
    if (value instanceof BeanCollection) {
      return ((BeanCollection)value).getActualDetails();
    } else {
      return (Collection)value;
    }
  }

  void jsonWriteCollection(SpiJsonWriter ctx, String name, Collection<?> list) throws IOException {
    if (!list.isEmpty() || ctx.isIncludeEmpty()) {
      ctx.beginAssocMany(name);
      for (Object bean : list) {
        jsonWriteElement(ctx, bean);
      }
      ctx.endAssocMany();
    }
  }

  void jsonWriteElement(SpiJsonWriter ctx, Object bean) throws IOException {
    targetDescriptor.jsonWrite(ctx, (EntityBean) bean);
  }
}
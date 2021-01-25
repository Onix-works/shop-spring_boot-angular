import { Product } from './product.model';

export interface HypermediaResult {
    _embedded: {
        products: Array<Product>
    };
    _links: {
      self: {
        href: string
      },
      [s: string]: {
        href: string
      }
    };
    page: {
    size,
    totalElements,
    totalPages,
    number
    };
}

import 'package:flutter/material.dart';

import '../../models/service_item.dart';
import '../../theme/app_theme.dart';
import '../service_detail_screen.dart';
import '../../services/booking_service.dart';
import '../../services/api_client.dart';

class ServicesTab extends StatefulWidget {
  final VoidCallback onNavigateToBookings;

  const ServicesTab({super.key, required this.onNavigateToBookings});

  @override
  State<ServicesTab> createState() => _ServicesTabState();
}

class _ServicesTabState extends State<ServicesTab> {
  ServiceCategory _selectedCategory = ServiceCategory.all;
  final TextEditingController _searchController = TextEditingController();
  final PageController _heroController = PageController(viewportFraction: 0.92);
  String _searchQuery = '';
  int _heroIndex = 0;
  List<ServiceItem> _services = ServiceItem.allServices;

  @override
  void initState() {
    super.initState();
    _loadServices();
  }

  Future<void> _loadServices() async {
    try {
      final response = await ApiClient.instance.get('/services') as List<dynamic>;
      if (!mounted) return;
      setState(() {
        final apiServices = response
            .map((item) => ServiceItem.fromApi(item as Map<String, dynamic>))
            .toList();
        final byId = {for (final item in ServiceItem.allServices) item.id: item};
        for (final item in apiServices) {
          byId[item.id] = item;
        }
        _services = byId.values.toList();
      });
    } catch (_) {
      // Keep the bundled catalog available when the API is offline.
    }
  }

  static const _slides = [
    _ServiceSlide(
      eyebrow: 'HOME MAINTENANCE',
      title: 'Repairs, right at home.',
      subtitle: 'Electricians, plumbing, AC and more.',
      imageUrl:
          'https://images.unsplash.com/photo-1621905251189-08b45d6a269e?auto=format&fit=crop&w=1200&q=85',
      category: ServiceCategory.homeMaintenance,
      fallbackColor: Color(0xFF174B3D),
    ),
    _ServiceSlide(
      eyebrow: 'CLEANING',
      title: 'A cleaner start.',
      subtitle: 'Find the right clean for your home.',
      imageUrl:
          'https://images.unsplash.com/photo-1581578731548-c64695cc6952?auto=format&fit=crop&w=1200&q=85',
      category: ServiceCategory.cleaning,
      fallbackColor: Color(0xFF315E4B),
    ),
    _ServiceSlide(
      eyebrow: 'BEAUTY & WELLNESS',
      title: 'Care, on your terms.',
      subtitle: 'Explore salon and wellness services.',
      imageUrl:
          'https://images.unsplash.com/photo-1560750588-73207b1ef5b8?auto=format&fit=crop&w=1200&q=85',
      category: ServiceCategory.beautyWomen,
      fallbackColor: Color(0xFF704D57),
    ),
    _ServiceSlide(
      eyebrow: 'EVERYDAY HELP',
      title: 'More time for your day.',
      subtitle: 'Browse home help and care services.',
      imageUrl:
          'https://images.unsplash.com/photo-1556911220-e15b29be8c8f?auto=format&fit=crop&w=1200&q=85',
      category: ServiceCategory.homeHelp,
      fallbackColor: Color(0xFF57513A),
    ),
  ];

  @override
  void dispose() {
    _searchController.dispose();
    _heroController.dispose();
    super.dispose();
  }

  void _selectSlideCategory(_ServiceSlide slide) {
    setState(() {
      _selectedCategory = slide.category;
      _searchController.clear();
      _searchQuery = '';
    });
  }

  List<ServiceItem> get _filteredServices {
    return _services.where((item) {
      final matchesCategory =
          _selectedCategory == ServiceCategory.all ||
          item.category == _selectedCategory;
      final matchesSearch =
          _searchQuery.isEmpty ||
          item.title.toLowerCase().contains(_searchQuery.toLowerCase()) ||
          item.description.toLowerCase().contains(_searchQuery.toLowerCase());
      return matchesCategory && matchesSearch;
    }).toList();
  }

  void _showCityPicker() {
    showModalBottomSheet<void>(
      context: context,
      shape: const RoundedRectangleBorder(
          borderRadius: BorderRadius.vertical(top: Radius.circular(12)),
      ),
      builder: (ctx) {
        return SafeArea(
          child: Column(
            mainAxisSize: MainAxisSize.min,
            crossAxisAlignment: CrossAxisAlignment.stretch,
            children: [
              const Padding(
                padding: EdgeInsets.fromLTRB(20, 16, 20, 8),
                child: Text(
                  'Select Your City (51+ Cities Operating)',
                  style: TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                    color: AppTheme.ink,
                  ),
                ),
              ),
              const Divider(),
              Expanded(
                child: ListView.builder(
                  itemCount: BookingService.availableCities.length,
                  itemBuilder: (context, idx) {
                    final city = BookingService.availableCities[idx];
                    final isSelected = city == BookingService().currentCity;
                    return ListTile(
                      title: Text(
                        city,
                        style: TextStyle(
                          fontWeight: isSelected
                              ? FontWeight.bold
                              : FontWeight.normal,
                        ),
                      ),
                      leading: Icon(
                        Icons.location_city_rounded,
                        color: isSelected
                            ? AppTheme.seed
                            : Colors.grey,
                      ),
                      trailing: isSelected
                          ? const Icon(
                              Icons.check_circle,
                              color: AppTheme.seed,
                            )
                          : null,
                      onTap: () {
                        BookingService().setCity(city);
                        Navigator.of(ctx).pop();
                        setState(() {});
                      },
                    );
                  },
                ),
              ),
            ],
          ),
        );
      },
    );
  }

  @override
  Widget build(BuildContext context) {
    final popularServices = _services
        .where((s) => s.isPopular)
        .toList();

    return Scaffold(
      backgroundColor: AppTheme.surface,
      appBar: AppBar(
        backgroundColor: Colors.white,
        elevation: 0,
        titleSpacing: 16,
        title: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Flexible(
                  child: const Text(
                    'Limitcross Facility',
                    overflow: TextOverflow.ellipsis,
                    style: TextStyle(
                      fontWeight: FontWeight.w800,
                      fontSize: 16,
                      color: AppTheme.ink,
                    ),
                  ),
                ),
              ],
            ),
            InkWell(
              onTap: _showCityPicker,
              child: Row(
                mainAxisSize: MainAxisSize.min,
                children: [
                  const Icon(
                    Icons.location_on,
                    size: 14,
                    color: AppTheme.seed,
                  ),
                  const SizedBox(width: 3),
                  Text(
                    BookingService().currentCity,
                    style: const TextStyle(
                      fontSize: 12,
                      fontWeight: FontWeight.w600,
                      color: AppTheme.mutedInk,
                    ),
                  ),
                  const Icon(
                    Icons.keyboard_arrow_down,
                    size: 16,
                    color: AppTheme.mutedInk,
                  ),
                ],
              ),
            ),
          ],
        ),
        actions: [
          IconButton(
            icon: const Icon(
              Icons.event_note_outlined,
              color: Color(0xFF0F172A),
            ),
            tooltip: 'My Bookings',
            onPressed: widget.onNavigateToBookings,
          ),
          const SizedBox(width: 8),
        ],
      ),
      body: CustomScrollView(
        slivers: [
          // Search & Metrics Banner
          SliverToBoxAdapter(
            child: Container(
              color: Colors.white,
              padding: const EdgeInsets.fromLTRB(16, 8, 16, 16),
              child: Column(
                children: [
                  // Search Bar
                  TextField(
                    controller: _searchController,
                    onChanged: (val) => setState(() => _searchQuery = val),
                    decoration: InputDecoration(
                      hintText: 'Search "AC repair", "Deep clean", "Salon"...',
                      prefixIcon: const Icon(
                        Icons.search_rounded,
                        color: Color(0xFF64748B),
                      ),
                      suffixIcon: _searchQuery.isNotEmpty
                          ? IconButton(
                              icon: const Icon(Icons.clear, size: 18),
                              onPressed: () {
                                _searchController.clear();
                                setState(() => _searchQuery = '');
                              },
                            )
                          : null,
                      filled: true,
                      fillColor: const Color(0xFFF1F5F9),
                      contentPadding: const EdgeInsets.symmetric(
                        horizontal: 16,
                        vertical: 12,
                      ),
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(8),
                        borderSide: BorderSide.none,
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),

          SliverToBoxAdapter(
            child: Column(
              children: [
                SizedBox(
                  height: 190,
                  child: PageView.builder(
                    controller: _heroController,
                    itemCount: _slides.length,
                    onPageChanged: (index) => setState(() => _heroIndex = index),
                    itemBuilder: (context, index) {
                      final slide = _slides[index];
                      final isActive = index == _heroIndex;
                      return AnimatedScale(
                        duration: const Duration(milliseconds: 220),
                        scale: isActive ? 1 : 0.96,
                        child: Padding(
                          padding: const EdgeInsets.fromLTRB(0, 12, 8, 8),
                          child: ClipRRect(
                            borderRadius: BorderRadius.circular(10),
                            child: Stack(
                              fit: StackFit.expand,
                              children: [
                                Image.network(
                                  slide.imageUrl,
                                  fit: BoxFit.cover,
                                  errorBuilder: (context, error, stackTrace) =>
                                      ColoredBox(color: slide.fallbackColor),
                                ),
                                const DecoratedBox(
                                  decoration: BoxDecoration(
                                    gradient: LinearGradient(
                                      begin: Alignment.centerLeft,
                                      end: Alignment.centerRight,
                                      colors: [
                                        Color(0xD9162D26),
                                        Color(0x15162D26),
                                      ],
                                    ),
                                  ),
                                ),
                                Positioned(
                                  left: 16,
                                  right: 16,
                                  bottom: 14,
                                  child: Column(
                                    crossAxisAlignment:
                                        CrossAxisAlignment.start,
                                    children: [
                                      Text(
                                        slide.eyebrow,
                                        style: const TextStyle(
                                          color: Color(0xFFD6F2E5),
                                          fontSize: 9,
                                          fontWeight: FontWeight.w700,
                                          letterSpacing: 1.1,
                                        ),
                                      ),
                                      const SizedBox(height: 5),
                                      Text(
                                        slide.title,
                                        maxLines: 1,
                                        overflow: TextOverflow.ellipsis,
                                        style: const TextStyle(
                                          color: Colors.white,
                                          fontSize: 21,
                                          fontWeight: FontWeight.w800,
                                        ),
                                      ),
                                      const SizedBox(height: 3),
                                      Text(
                                        slide.subtitle,
                                        maxLines: 1,
                                        overflow: TextOverflow.ellipsis,
                                        style: TextStyle(
                                          color: Colors.white.withValues(
                                            alpha: 0.92,
                                          ),
                                          fontSize: 11,
                                        ),
                                      ),
                                      const SizedBox(height: 7),
                                      SizedBox(
                                        height: 30,
                                        child: FilledButton.tonalIcon(
                                          onPressed: () =>
                                              _selectSlideCategory(slide),
                                          icon: const Icon(
                                            Icons.arrow_forward,
                                            size: 15,
                                          ),
                                          label: const Text('Explore services'),
                                          style: FilledButton.styleFrom(
                                            backgroundColor: Colors.white,
                                            foregroundColor:
                                                const Color(0xFF173B32),
                                            padding: const EdgeInsets.symmetric(
                                              horizontal: 10,
                                            ),
                                            textStyle: const TextStyle(
                                              fontSize: 11,
                                              fontWeight: FontWeight.w700,
                                            ),
                                          ),
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                      );
                    },
                  ),
                ),
                Padding(
                  padding: const EdgeInsets.fromLTRB(16, 0, 16, 8),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: List.generate(_slides.length, (index) {
                      final isActive = index == _heroIndex;
                      return Semantics(
                        button: true,
                        label: 'Show slide ${index + 1} of ${_slides.length}',
                        child: GestureDetector(
                          onTap: () => _heroController.animateToPage(
                            index,
                            duration: const Duration(milliseconds: 300),
                            curve: Curves.easeOut,
                          ),
                          child: AnimatedContainer(
                            duration: const Duration(milliseconds: 200),
                            width: isActive ? 22 : 7,
                            height: 7,
                            margin: const EdgeInsets.symmetric(horizontal: 3),
                            decoration: BoxDecoration(
                              color: isActive
                                  ? const Color(0xFF176B57)
                                  : const Color(0xFFC8D2CC),
                              borderRadius: BorderRadius.circular(8),
                            ),
                          ),
                        ),
                      );
                    }),
                  ),
                ),
              ],
            ),
          ),

          // Categories Horizontal Selector
          SliverToBoxAdapter(
            child: Container(
              color: Colors.white,
              padding: const EdgeInsets.only(bottom: 12),
              child: SingleChildScrollView(
                scrollDirection: Axis.horizontal,
                padding: const EdgeInsets.symmetric(horizontal: 16),
                child: Row(
                  children: ServiceCategory.values.map((cat) {
                    final isSelected = _selectedCategory == cat;
                    return Padding(
                      padding: const EdgeInsets.only(right: 8),
                      child: FilterChip(
                        avatar: Icon(cat.icon, size: 16),
                        label: Text(cat.label),
                        selected: isSelected,
                        onSelected: (val) {
                          setState(() => _selectedCategory = cat);
                        },
                        selectedColor: AppTheme.seed,
                        labelStyle: TextStyle(
                          color: isSelected
                              ? Colors.white
                              : AppTheme.ink,
                          fontWeight: isSelected
                              ? FontWeight.bold
                              : FontWeight.w500,
                          fontSize: 12,
                        ),
                        backgroundColor: AppTheme.surface,
                        showCheckmark: false,
                        shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.circular(8),
                          side: BorderSide(
                            color: isSelected
                                ? AppTheme.seed
                                : AppTheme.border,
                          ),
                        ),
                      ),
                    );
                  }).toList(),
                ),
              ),
            ),
          ),

          // Popular Carousel (Show when no search active)
          if (_searchQuery.isEmpty &&
              _selectedCategory == ServiceCategory.all) ...[
            SliverToBoxAdapter(
              child: Padding(
                padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Expanded(
                      child: Text(
                        'Popular in ${BookingService().currentCity}',
                        style: TextStyle(
                          fontSize: 14,
                          fontWeight: FontWeight.bold,
                          color: Color(0xFF0F172A),
                        ),
                        overflow: TextOverflow.ellipsis,
                      ),
                    ),
                    const SizedBox(width: 8),
                    Text(
                      '${popularServices.length} services',
                      style: TextStyle(
                        fontSize: 12,
                        color: Colors.grey.shade600,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            SliverToBoxAdapter(
              child: SizedBox(
                height: 156,
                child: ListView.builder(
                  scrollDirection: Axis.horizontal,
                  padding: const EdgeInsets.symmetric(horizontal: 16),
                  itemCount: popularServices.length,
                  itemBuilder: (context, index) {
                    final s = popularServices[index];
                    return InkWell(
                      borderRadius: BorderRadius.circular(8),
                      onTap: () {
                        Navigator.of(context).push(
                          MaterialPageRoute<void>(
                            builder: (_) => ServiceDetailScreen(
                              service: s,
                              onBookingCreated: widget.onNavigateToBookings,
                            ),
                          ),
                        );
                      },
                      child: Container(
                        width: 200,
                        margin: const EdgeInsets.only(right: 12),
                        padding: const EdgeInsets.all(12),
                        decoration: BoxDecoration(
                          color: Colors.white,
                          borderRadius: BorderRadius.circular(8),
                          border: Border.all(color: AppTheme.border),
                          boxShadow: [
                            BoxShadow(
                              color: Colors.black.withValues(alpha: 0.03),
                              blurRadius: 8,
                              offset: const Offset(0, 2),
                            ),
                          ],
                        ),
                        child: Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Row(
                              children: [
                                Container(
                                  width: 36,
                                  height: 36,
                                  decoration: BoxDecoration(
                                    color: s.accentColor.withValues(
                                      alpha: 0.12,
                                    ),
                                    borderRadius: BorderRadius.circular(8),
                                  ),
                                  alignment: Alignment.center,
                                  child: Icon(
                                    s.category.icon,
                                    size: 19,
                                    color: s.accentColor,
                                  ),
                                ),
                                const Spacer(),
                                Container(
                                  padding: const EdgeInsets.symmetric(
                                    horizontal: 6,
                                    vertical: 2,
                                  ),
                                  decoration: BoxDecoration(
                                    color: Colors.amber.shade50,
                                    borderRadius: BorderRadius.circular(6),
                                  ),
                                  child: Row(
                                    mainAxisSize: MainAxisSize.min,
                                    children: [
                                      const Icon(
                                        Icons.star_rounded,
                                        size: 14,
                                        color: Colors.amber,
                                      ),
                                      const SizedBox(width: 2),
                                      Text(
                                        '${s.rating}',
                                        style: const TextStyle(
                                          fontSize: 11,
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                                    ],
                                  ),
                                ),
                              ],
                            ),
                            const SizedBox(height: 8),
                            Text(
                              s.title,
                              maxLines: 1,
                              overflow: TextOverflow.ellipsis,
                              style: const TextStyle(
                                fontWeight: FontWeight.bold,
                                fontSize: 13,
                                color: Color(0xFF0F172A),
                              ),
                            ),
                            Text(
                              s.priceRange,
                              style: const TextStyle(
                                fontWeight: FontWeight.w600,
                                fontSize: 12,
                                color: Color(0xFF10B981),
                              ),
                            ),
                            const Spacer(),
                            SizedBox(
                              height: 30,
                              width: double.infinity,
                              child: FilledButton(
                                onPressed: () => Navigator.of(context).push(
                                  MaterialPageRoute<void>(
                                    builder: (_) => ServiceDetailScreen(
                                      service: s,
                                      onBookingCreated:
                                          widget.onNavigateToBookings,
                                    ),
                                  ),
                                ),
                                style: FilledButton.styleFrom(
                                  backgroundColor: const Color(0xFF0F172A),
                                  padding: EdgeInsets.zero,
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(8),
                                  ),
                                ),
                                child: const Text(
                                  'Details',
                                  style: TextStyle(
                                    fontSize: 12,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    );
                  },
                ),
              ),
            ),
          ],

          // All / Filtered Services Section Header
          SliverToBoxAdapter(
            child: Padding(
              padding: const EdgeInsets.fromLTRB(16, 20, 16, 10),
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Expanded(
                    child: Text(
                      _selectedCategory == ServiceCategory.all
                          ? 'All Available Services'
                          : _selectedCategory.label,
                      style: const TextStyle(
                        fontSize: 15,
                        fontWeight: FontWeight.bold,
                        color: Color(0xFF0F172A),
                      ),
                      overflow: TextOverflow.ellipsis,
                    ),
                  ),
                  const SizedBox(width: 8),
                  Text(
                    '${_filteredServices.length} found',
                    style: TextStyle(fontSize: 12, color: Colors.grey.shade600),
                  ),
                ],
              ),
            ),
          ),

          // Services Grid / List
          _filteredServices.isEmpty
              ? SliverToBoxAdapter(
                  child: Container(
                    padding: const EdgeInsets.all(40),
                    alignment: Alignment.center,
                    child: Column(
                      children: [
                        const Icon(
                          Icons.search_off_rounded,
                          size: 48,
                          color: Colors.grey,
                        ),
                        const SizedBox(height: 12),
                        Text(
                          'No services found for "$_searchQuery"',
                          style: const TextStyle(
                            fontWeight: FontWeight.w600,
                            color: Color(0xFF64748B),
                          ),
                        ),
                      ],
                    ),
                  ),
                )
              : SliverPadding(
                  padding: const EdgeInsets.fromLTRB(16, 0, 16, 24),
                  sliver: SliverList(
                    delegate: SliverChildBuilderDelegate((context, index) {
                      final s = _filteredServices[index];
                      return InkWell(
                        borderRadius: BorderRadius.circular(8),
                        onTap: () {
                          Navigator.of(context).push(
                            MaterialPageRoute<void>(
                              builder: (_) => ServiceDetailScreen(
                                service: s,
                                onBookingCreated: widget.onNavigateToBookings,
                              ),
                            ),
                          );
                        },
                        child: Container(
                          margin: const EdgeInsets.only(bottom: 12),
                          padding: const EdgeInsets.all(14),
                          decoration: BoxDecoration(
                            color: Colors.white,
                            borderRadius: BorderRadius.circular(8),
                            border: Border.all(color: AppTheme.border),
                            boxShadow: [
                              BoxShadow(
                                color: Colors.black.withValues(alpha: 0.02),
                                blurRadius: 6,
                                offset: const Offset(0, 2),
                              ),
                            ],
                          ),
                          child: Row(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Container(
                                width: 50,
                                height: 50,
                                decoration: BoxDecoration(
                                  color: s.accentColor.withValues(alpha: 0.12),
                                  borderRadius: BorderRadius.circular(8),
                                ),
                                alignment: Alignment.center,
                                child: Icon(
                                  s.category.icon,
                                  size: 22,
                                  color: s.accentColor,
                                ),
                              ),
                              const SizedBox(width: 14),
                              Expanded(
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Row(
                                      children: [
                                        Expanded(
                                          child: Text(
                                            s.title,
                                            style: const TextStyle(
                                              fontWeight: FontWeight.bold,
                                              fontSize: 15,
                                              color: Color(0xFF0F172A),
                                            ),
                                          ),
                                        ),
                                        Container(
                                          padding: const EdgeInsets.symmetric(
                                            horizontal: 6,
                                            vertical: 2,
                                          ),
                                          decoration: BoxDecoration(
                                            color: Colors.amber.shade50,
                                            borderRadius: BorderRadius.circular(
                                              6,
                                            ),
                                          ),
                                          child: Row(
                                            mainAxisSize: MainAxisSize.min,
                                            children: [
                                              const Icon(
                                                Icons.star_rounded,
                                                size: 14,
                                                color: Colors.amber,
                                              ),
                                              const SizedBox(width: 2),
                                              Text(
                                                '${s.rating}',
                                                style: const TextStyle(
                                                  fontSize: 11,
                                                  fontWeight: FontWeight.bold,
                                                ),
                                              ),
                                            ],
                                          ),
                                        ),
                                      ],
                                    ),
                                    const SizedBox(height: 4),
                                    Text(
                                      s.description,
                                      style: TextStyle(
                                        fontSize: 12,
                                        color: Colors.grey.shade600,
                                      ),
                                    ),
                                    const SizedBox(height: 8),
                                    Row(
                                      mainAxisAlignment:
                                          MainAxisAlignment.spaceBetween,
                                      children: [
                                        Flexible(
                                          child: Text(
                                            s.priceRange,
                                            overflow: TextOverflow.ellipsis,
                                            style: const TextStyle(
                                              fontWeight: FontWeight.bold,
                                              fontSize: 14,
                                              color: Color(0xFF059669),
                                            ),
                                          ),
                                        ),
                                        FilledButton(
                                          onPressed: () =>
                                              Navigator.of(context).push(
                                                MaterialPageRoute<void>(
                                                  builder: (_) =>
                                                      ServiceDetailScreen(
                                                        service: s,
                                                        onBookingCreated: widget
                                                            .onNavigateToBookings,
                                                      ),
                                                ),
                                              ),
                                          style: FilledButton.styleFrom(
                                            backgroundColor: const Color(
                                              0xFF0F172A,
                                            ),
                                            minimumSize: const Size(72, 34),
                                            padding: const EdgeInsets.symmetric(
                                              horizontal: 10,
                                            ),
                                            shape: RoundedRectangleBorder(
                                              borderRadius:
                                                  BorderRadius.circular(8),
                                            ),
                                          ),
                                          child: const Text(
                                            'Details',
                                            style: TextStyle(
                                              fontSize: 12,
                                              fontWeight: FontWeight.bold,
                                            ),
                                          ),
                                        ),
                                      ],
                                    ),
                                  ],
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    }, childCount: _filteredServices.length),
                  ),
                ),
        ],
      ),
    );
  }
}

class _ServiceSlide {
  const _ServiceSlide({
    required this.eyebrow,
    required this.title,
    required this.subtitle,
    required this.imageUrl,
    required this.category,
    required this.fallbackColor,
  });

  final String eyebrow;
  final String title;
  final String subtitle;
  final String imageUrl;
  final ServiceCategory category;
  final Color fallbackColor;
}
